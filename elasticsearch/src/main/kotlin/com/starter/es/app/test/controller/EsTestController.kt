package com.starter.es.app.test.controller

import com.starter.core.common.response.SuccessResponse
import com.starter.es.app.test.service.LowLevelEsService
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PutMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/api/es-test")
class EsTestController(
    private val lowLevelEsService: LowLevelEsService,
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


}
