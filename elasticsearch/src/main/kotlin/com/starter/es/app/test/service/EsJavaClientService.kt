package com.starter.es.app.test.service

import co.elastic.clients.elasticsearch.ElasticsearchClient
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
import java.time.ZoneOffset
import java.time.ZonedDateTime

@Service
class EsJavaClientService(
    private val elasticsearchClient: ElasticsearchClient,
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

    private fun createNow() = ZonedDateTime.now(ZoneOffset.UTC)
}
