package com.starter.es.config.es

import co.elastic.clients.elasticsearch.ElasticsearchClient
import co.elastic.clients.json.jackson.JacksonJsonpMapper
import co.elastic.clients.transport.rest_client.RestClientOptions
import co.elastic.clients.transport.rest_client.RestClientTransport
import com.fasterxml.jackson.databind.ObjectMapper
import org.apache.http.HttpHost
import org.elasticsearch.action.ActionListener
import org.elasticsearch.action.bulk.BackoffPolicy
import org.elasticsearch.action.bulk.BulkProcessor
import org.elasticsearch.action.bulk.BulkRequest
import org.elasticsearch.action.bulk.BulkResponse
import org.elasticsearch.client.HttpAsyncResponseConsumerFactory
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
class EsConfig(
    private val objectMapper: ObjectMapper,
) {
    companion object {
        private const val ES_CONNECTION_TIMEOUT = 5000 // 5s
        private const val ES_SOCKET_TIMEOUT = 5000 // 5s
        private const val CLIENT_BUFFER_SIZE = 500 * 1024 * 1024 // 500MB
    }

    @Bean("lowLevelEsRestClient")
    fun lowLevelEsRestClient(): RestClient {
        return createBaseRestClientBuilder()
            .build()
    }

    @Bean
    fun esJavaClient(
        @Qualifier("lowLevelEsRestClient") lowLevelEsRestClient: RestClient,
    ): ElasticsearchClient {
        val restClientOptions = RestClientOptions(
            RequestOptions.DEFAULT
                .toBuilder()
                .setHttpAsyncResponseConsumerFactory(
                    HttpAsyncResponseConsumerFactory.HeapBufferedResponseConsumerFactory(CLIENT_BUFFER_SIZE),
                )
                .build(),
        )
        val transport = RestClientTransport(lowLevelEsRestClient, JacksonJsonpMapper(objectMapper), restClientOptions,)
        return ElasticsearchClient(transport)
    }

    @Bean("highLevelEsRestClient")
    fun highLevelEsRestClient(
        @Qualifier("lowLevelEsRestClient") lowLevelEsRestClient: RestClient,
    ): RestHighLevelClient {
        return RestHighLevelClientBuilder(lowLevelEsRestClient)
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
