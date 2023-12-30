package com.starter.es.app.test.service

import mu.KLogging
import org.apache.commons.lang3.builder.ToStringBuilder
import org.elasticsearch.action.bulk.BulkProcessor
import org.elasticsearch.action.bulk.BulkRequest
import org.elasticsearch.action.get.GetRequest
import org.elasticsearch.action.index.IndexRequest
import org.elasticsearch.action.search.SearchRequest
import org.elasticsearch.action.update.UpdateRequest
import org.elasticsearch.client.RequestOptions
import org.elasticsearch.client.RestHighLevelClient
import org.elasticsearch.index.query.MatchQueryBuilder
import org.elasticsearch.index.query.Operator
import org.elasticsearch.index.query.QueryBuilders
import org.elasticsearch.index.query.RangeQueryBuilder
import org.elasticsearch.index.query.TermQueryBuilder
import org.elasticsearch.search.builder.SearchSourceBuilder
import org.elasticsearch.xcontent.XContentType
import org.springframework.stereotype.Service

@Service
class HighLevelEsService(
    private val highLevelEsRestClient: RestHighLevelClient,
    private val bulkProcessor: BulkProcessor,
) {
    companion object : KLogging()

    fun getSample(): String {
        val req = GetRequest()
            .index("my-index-01")
            .id("document-id-01")
            .routing("abc123")

        val res = highLevelEsRestClient.get(req, RequestOptions.DEFAULT)
        logger.debug { "[ES_TEST] res : $res" }
        return res.sourceAsString
    }

    fun searchSample(): List<MutableMap<String, Any>> {
        val queryBuilder = QueryBuilders.boolQuery()
            .must(TermQueryBuilder("filedOne", "hello"))
            .should(MatchQueryBuilder("filedTwo", "hello world").operator(Operator.AND))
            .should(RangeQueryBuilder("filedThree").gte(100).lt(200))
            .minimumShouldMatch(1)

        val searchSourceBuilder = SearchSourceBuilder()
            .from(0)
            .size(10)
            .query(queryBuilder)

        val searchRequest = SearchRequest()
            .indices("my-index-01", "my-index-02")
            .routing("abc123")
            .source(searchSourceBuilder)

        val res = highLevelEsRestClient.search(searchRequest, RequestOptions.DEFAULT)
        logger.debug { "[ES_TEST] res : $res" }
        val searchHits = res.hits
        val totalHits = searchHits.totalHits
        logger.debug { "[ES_TEST] totalHits : $totalHits" }
        return searchHits
            .hits
            .map { it.sourceAsMap }
    }

    fun bulk() {
        val bulkRequest = BulkRequest()
        bulkRequest.add(
            UpdateRequest()
                .index("my-index-01")
                .id("document-id-01")
                .routing("abc123")
                .doc(mapOf("hello" to "elasticsearch")),
        )

        val bulkResponse = highLevelEsRestClient.bulk(bulkRequest, RequestOptions.DEFAULT)
        if (bulkResponse.hasFailures()) {
            logger.error("[ES_TEST] ${bulkResponse.buildFailureMessage()}")
        }
        bulkResponse.items
        logger.debug { "[ES_TEST] bulkResponse : ${ToStringBuilder.reflectionToString(bulkResponse)} " }
    }

    fun bulkProcessor(id: String) {
        val source = mapOf<String, Any>(
            "hello" to "world",
            "world" to 123,
            "name" to "name-$id",
        )

        val indexRequest = IndexRequest("my-index-01")
            .id(id)
            .routing("abc123")
            .source(source, XContentType.JSON)

        bulkProcessor.add(indexRequest)
    }
}
