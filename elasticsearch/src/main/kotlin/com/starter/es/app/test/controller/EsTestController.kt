package com.starter.es.app.test.controller

import com.starter.core.common.response.SuccessResponse
import com.starter.es.app.test.service.HighLevelEsService
import com.starter.es.app.test.service.LowLevelEsService
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.PutMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/api/es-test")
class EsTestController(
    private val lowLevelEsService: LowLevelEsService,
    private val highLevelEsService: HighLevelEsService,
) {

    @GetMapping("/cluster-settings")
    fun clusterSettings(): SuccessResponse<String> {
        val result = lowLevelEsService.getClusterSettings()
        return SuccessResponse.of(result)
    }

    @PutMapping("/cluster-settings/async")
    fun asyncUpdateClusterSettings(): SuccessResponse<String> {
        lowLevelEsService.asyncUpdateSetting()
        return SuccessResponse.DEFAULT
    }

    @GetMapping("/samples")
    fun getSamples(): SuccessResponse<String> {
        val result = highLevelEsService.getSample()
        return SuccessResponse.of(result)
    }

    @GetMapping("/samples/search")
    fun searchSamples(): SuccessResponse<List<Map<String, Any>>> {
        val results = highLevelEsService.searchSample()
        return SuccessResponse.of(results)
    }

    @PostMapping("/bulk")
    fun bulk(): SuccessResponse<String> {
        highLevelEsService.bulk()
        return SuccessResponse.DEFAULT
    }

    @PostMapping("/bulk/processor")
    fun bulkProcessor(@RequestBody body: BulkProcessorBody): SuccessResponse<String> {
        highLevelEsService.bulkProcessor(body.id)
        return SuccessResponse.DEFAULT
    }

    data class BulkProcessorBody(
        val id: String,
    )
}
