package com.starter.es.config.es

import mu.KLogging
import org.apache.commons.lang3.builder.ToStringBuilder
import org.elasticsearch.action.bulk.BulkProcessor
import org.elasticsearch.action.bulk.BulkRequest
import org.elasticsearch.action.bulk.BulkResponse

class MyEsBulkListener : BulkProcessor.Listener {
    companion object : KLogging()

    override fun beforeBulk(executionId: Long, request: BulkRequest) {
        logger.debug { "[ES_TEST] before bulk" }
    }

    override fun afterBulk(executionId: Long, request: BulkRequest, response: BulkResponse) {
        if (!response.hasFailures()) {
            logger.debug { "[ES_TEST] bulk success" }
        } else {
            logger.error { "[ES_TEST] bulk failures" }
            val failedItems = response.items
                .filter { it.isFailed }

            logger.error { "[ES_TEST] failed items : ${ToStringBuilder.reflectionToString(failedItems)}" }
        }
    }

    override fun afterBulk(executionId: Long, request: BulkRequest?, failure: Throwable?) {
        logger.debug { "[ES_TEST] after bulk" }
    }
}
