package com.starter.es.app.test.service

import mu.KLogging
import org.apache.http.entity.ContentType
import org.apache.http.nio.entity.NStringEntity
import org.apache.http.util.EntityUtils
import org.elasticsearch.client.Request
import org.elasticsearch.client.Response
import org.elasticsearch.client.ResponseListener
import org.elasticsearch.client.RestClient
import org.springframework.stereotype.Service
import java.nio.charset.StandardCharsets

@Service
class LowLevelEsService(
    private val lowLevelEsRestClient: RestClient,
) {
    companion object : KLogging()

    fun getClusterSettings(): String {
        val req = Request("GET", "/_cluster/settings")
        req.addParameters(
            mapOf(
                "pretty" to "true",
            ),
        )

        logger.debug { "[ES TEST] start request" }
        val res = lowLevelEsRestClient.performRequest(req)
        logger.debug { "[ES TEST] response : $res" }
        return getBody(res)
    }

    fun asyncUpdateSetting() {
        val req = Request("PUT", "/_cluster/settings")
        val requestBody = """
            {
                "transient": {
                    "cluster.routing.allocation.enable": "all"
                }
            }
        """.trimIndent()

        req.entity = NStringEntity(requestBody, ContentType.APPLICATION_JSON)

        logger.debug { "[ES TEST] start request" }
        val cancellable = lowLevelEsRestClient.performRequestAsync(
            req,
            object : ResponseListener {
                override fun onSuccess(response: Response) {
                    logger.debug { "[ES TEST] response : $response" }
                }

                override fun onFailure(ex: Exception) {
                    logger.error("[ES TEST] es error", ex)
                }
            },
        )

        logger.debug { "[ES TEST] thread sleep" }
        Thread.sleep(1000L)
        cancellable.cancel()
    }

    private fun getBody(response: Response): String {
        val statusCode = response.statusLine.statusCode
        val responseBody = EntityUtils.toString(response.entity, StandardCharsets.UTF_8)
        logger.debug { "[ES TEST] statusCode : $statusCode, body : $responseBody" }
        return responseBody
    }
}
