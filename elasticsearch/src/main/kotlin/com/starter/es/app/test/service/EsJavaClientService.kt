package com.starter.es.app.test.service

import co.elastic.clients.elasticsearch.ElasticsearchClient
import co.elastic.clients.elasticsearch._helpers.bulk.BulkIngester
import co.elastic.clients.elasticsearch.core.GetRequest
import co.elastic.clients.elasticsearch.core.IndexRequest
import co.elastic.clients.elasticsearch.core.SearchRequest
import co.elastic.clients.elasticsearch.core.bulk.BulkOperation
import co.elastic.clients.elasticsearch.core.bulk.CreateOperation
import co.elastic.clients.elasticsearch.core.bulk.IndexOperation
import co.elastic.clients.elasticsearch.core.bulk.UpdateAction
import co.elastic.clients.elasticsearch.core.bulk.UpdateOperation
import com.starter.es.app.test.models.index.MyIndexClass
import com.starter.es.app.test.models.index.MyPartialIndexClass
import mu.KLogging
import org.springframework.stereotype.Service
import java.time.LocalDateTime
import java.time.ZoneOffset
import java.time.ZonedDateTime

@Service
class EsJavaClientService(
    private val elasticsearchClient: ElasticsearchClient,
    private val bulkIngester: BulkIngester<String>,
) {
    companion object : KLogging() {
        private const val INDEX_NAME = "my-index"
    }

    fun indexExample(): String {
        val indexRequest = IndexRequest.Builder<MyIndexClass>()
            .index(INDEX_NAME)
            .id("my-id-1")
            .routing("my-routing-1")
            .document(MyIndexClass("hello", 1L, createNow()))
            .build()

        val response = elasticsearchClient.index(indexRequest)
        val result = response.result()
        logger.debug { "[ES_TEST] response : $response , resultName : ${result.name}" }
        return response.toString()
    }

    fun getSample(id: String): MyIndexClass? {
        val getRequest = GetRequest.Builder()
            .index(INDEX_NAME)
            .id(id)
            .routing("my-routing-1")
            .build()

        val response = elasticsearchClient.get(getRequest, MyIndexClass::class.java)
        val result = response.source()
        logger.debug { "[ES_TEST] response : $response , result : $result" }
        return result
    }

    fun bulk() {
        val createOperation = CreateOperation.Builder<MyIndexClass>()
            .index(INDEX_NAME)
            .id("my-id-2")
            .routing("my-routing-2")
            .document(MyIndexClass("world", 2L, createNow()))
            .build()

        val indexOperation = IndexOperation.Builder<MyIndexClass>()
            .index(INDEX_NAME)
            .id("my-id-3")
            .routing("my-routing-3")
            .document(MyIndexClass("world", 4L, createNow()))
            .build()

        val updateAction = UpdateAction.Builder<MyIndexClass, MyPartialIndexClass>()
            .doc(MyPartialIndexClass("world updated"))
            .build()

        val updateOperation = UpdateOperation.Builder<MyIndexClass, MyPartialIndexClass>()
            .index(INDEX_NAME)
            .id("my-id-1")
            .routing("my-routing-1")
            .action(updateAction)
            .build()

        val bulkOpOne = BulkOperation.Builder().create(createOperation).build()
        val bulkOpTwo = BulkOperation.Builder().index(indexOperation).build()
        val bulkOpThree = BulkOperation.Builder().update(updateOperation).build()
        val operations = listOf<BulkOperation>(bulkOpOne, bulkOpTwo, bulkOpThree)

        val bulkResponse = elasticsearchClient.bulk { it.operations(operations) }

        for (item in bulkResponse.items()) {
            logger.debug { "[ES_TEST] results : ${item.result()}, error : ${item.error()}" }
        }
    }

    fun search(fieldOneValue: String): List<MyIndexClass> {
        val searchRequest = SearchRequest.Builder()
            .index(INDEX_NAME)
            .from(0)
            .size(10)
            .query { q ->
                q.term { t ->
                    t.field("fieldOne")
                        .value { v -> v.stringValue(fieldOneValue) }
                }
            }
            .build()
        val response = elasticsearchClient.search(searchRequest, MyIndexClass::class.java)
        val hits = response.hits().hits()
        for (item in hits) {
            logger.debug { "[ES_TEST] source : ${item.source()}" }
        }

        return hits.mapNotNull { it.source() }
    }

    fun bulkWithIngester() {
        val startDatetime = LocalDateTime.now()
        val epochSecond = startDatetime.toEpochSecond(ZoneOffset.UTC)
        logger.debug { "[ES_TEST] startEpochSecond : $epochSecond" }
        for (number in 0L..1000L) {
            val bulkOperation = BulkOperation.of { builder ->
                builder.index { indexOpBUilder ->
                    indexOpBUilder
                        .index(INDEX_NAME)
                        .id("my-id-$number")
                        .routing("my-routing-$number")
                        .document(MyIndexClass("world", concatNumber(epochSecond, number), createNow()))
                }
            }

            bulkIngester.add(bulkOperation, "my-context-$number")
        }

        logger.debug { "[ES_TEST] [${LocalDateTime.now()}] sleep 10 seconds ..." }
        Thread.sleep(10000L)

        for (number in 1001L..1500L) {
            val bulkOperation = BulkOperation.of { builder ->
                builder.index { indexOpBUilder ->
                    indexOpBUilder
                        .index(INDEX_NAME)
                        .id("my-id-$number")
                        .routing("my-routing-$number")
                        .document(MyIndexClass("world", concatNumber(epochSecond, number), createNow()))
                }
            }

            bulkIngester.add(bulkOperation, "my-context-$number")
        }

        logger.debug { "[ES_TEST] [${LocalDateTime.now()}] sleep 10 seconds ..." }
        Thread.sleep(10000L)

        logger.debug { "[ES_TEST] It's completed." }

        // bean이므로 굳이 닫지않음.
        // bulkIngester.close()
    }

    private fun createNow() = ZonedDateTime.now(ZoneOffset.UTC)

    private fun concatNumber(baseNumber: Long, number: Long): Long {
        // 결과를 String으로 변환하고 나머지 빈 자리를 0으로 채웁니다.
        return String.format("%d%03d", baseNumber, number).toLong()
    }
}
