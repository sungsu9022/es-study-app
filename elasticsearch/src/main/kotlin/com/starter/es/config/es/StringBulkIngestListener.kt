package com.starter.es.config.es

import co.elastic.clients.elasticsearch._helpers.bulk.BulkListener
import co.elastic.clients.elasticsearch.core.BulkRequest
import co.elastic.clients.elasticsearch.core.BulkResponse
import mu.KLogging
import java.time.LocalDateTime

class StringBulkIngestListener<Context> : BulkListener<Context> {
    companion object : KLogging()

    override fun beforeBulk(executionId: Long, request: BulkRequest, contexts: MutableList<Context>) {
        val now = LocalDateTime.now()
        logger.debug { "[ES_TEST] [$now] beforeBulk - executionId : $executionId" }
    }

    override fun afterBulk(
        executionId: Long,
        request: BulkRequest,
        contexts: MutableList<Context>,
        response: BulkResponse,
    ) {
        val now = LocalDateTime.now()
        logger.info { "[ES_TEST] [$now] afterBulk - executionId : $executionId, itemSize : ${response.items().size}, took : ${response.took()}, errors : ${response.errors()}" }
    }

    override fun afterBulk(
        executionId: Long,
        request: BulkRequest,
        contexts: MutableList<Context>,
        failure: Throwable,
    ) {
        val now = LocalDateTime.now()
        logger.error("[ES_TEST] [$now] afterBulk failure - executionId : $executionId", failure)
    }
}
