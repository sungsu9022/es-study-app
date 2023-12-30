package com.starter.es.config

import org.apache.http.HttpHost
import org.elasticsearch.client.RestClient
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
        return RestClient
            .builder(
                HttpHost("localhost", 9200, "http"),
//                HttpHost("es02", 9200, "http"),
//                HttpHost("es03", 9200, "http"),
            )
            .setRequestConfigCallback {
                it.setConnectTimeout(ES_CONNECTION_TIMEOUT)
                it.setSocketTimeout(ES_SOCKET_TIMEOUT)
            }
            .build()
    }
}
