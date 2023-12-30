package com.starter.es.config.es

import org.apache.http.HttpHost
import org.elasticsearch.action.ActionListener
import org.elasticsearch.action.bulk.BackoffPolicy
import org.elasticsearch.action.bulk.BulkProcessor
import org.elasticsearch.action.bulk.BulkRequest
import org.elasticsearch.action.bulk.BulkResponse
import org.elasticsearch.client.RequestOptions
import org.elasticsearch.client.RestClient
import org.elasticsearch.client.RestClientBuilder
import org.elasticsearch.client.RestHighLevelClient
import org.elasticsearch.client.RestHighLevelClientBuilder
import org.elasticsearch.common.unit.ByteSizeValue
import org.elasticsearch.core.TimeValue
import org.springframework.beans.factory.annotation.Qualifier
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration

@Configuration
class EsConfig {
    companion object {
        private const val ES_CONNECTION_TIMEOUT = 5000 // 5s
        private const val ES_SOCKET_TIMEOUT = 5000 // 5s
    }

    @Bean("lowLevelEsRestClient")
    fun lowLevelEsRestClient(): RestClient {
        return createBaseRestClientBuilder()
            .build()
    }

    @Bean("highLevelEsRestClient")
    fun highLevelEsRestClient(): RestHighLevelClient {
        val builder = createBaseRestClientBuilder()
        return RestHighLevelClientBuilder(builder.build())
            // NOTE es 8 이상일 경우 서버 호환을 위해 true로 설정
            .setApiCompatibilityMode(true)
            .build()
    }

    @Bean
    fun bulkProcessor(
        @Qualifier("highLevelEsRestClient") highLevelEsRestClient: RestHighLevelClient,
    ): BulkProcessor {
        val bulkAsync = {
                request: BulkRequest, listener: ActionListener<BulkResponse> ->
            highLevelEsRestClient.bulkAsync(request, RequestOptions.DEFAULT, listener)
            Unit
        }

        return BulkProcessor
            .builder(bulkAsync, MyEsBulkListener(), "myBulkProcessor")
            .setBulkActions(50000)
            .setBulkSize(ByteSizeValue.ofMb(50L))
            .setFlushInterval(TimeValue.timeValueMillis(5000L))
            .setConcurrentRequests(1)
            .setBackoffPolicy(BackoffPolicy.exponentialBackoff())
            .build()
    }

    private fun createBaseRestClientBuilder(): RestClientBuilder {
        return RestClient
            .builder(
                HttpHost("localhost", 9200, "http"),
            )
            .setRequestConfigCallback {
                it.setConnectTimeout(ES_CONNECTION_TIMEOUT)
                it.setSocketTimeout(ES_SOCKET_TIMEOUT)
            }
    }
}
