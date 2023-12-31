package com.starter.es.app.test.controller

import com.starter.core.common.response.SuccessResponse
import com.starter.es.app.test.models.index.MyIndexClass
import com.starter.es.app.test.service.EsJavaClientService
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.PutMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RequestParam
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/api/es-test/java")
class EsJavaClientTestController(
    private val esJavaClientService: EsJavaClientService,
) {

    @PutMapping("/samples/index")
    fun indexSample(): SuccessResponse<String> {
        val result = esJavaClientService.indexExample()
        return SuccessResponse.of(result)
    }

    @GetMapping("/samples/{id}")
    fun getSample(@PathVariable id: String): SuccessResponse<MyIndexClass?> {
        val result = esJavaClientService.getSample(id)
        return SuccessResponse.of(result)
    }

    @PostMapping("/bulk")
    fun bulk(): SuccessResponse<String> {
        esJavaClientService.bulk()
        return SuccessResponse.DEFAULT
    }

    @GetMapping("/samples/search")
    fun search(@RequestParam fieldOneValue: String): SuccessResponse<List<MyIndexClass>> {
        val result = esJavaClientService.search(fieldOneValue)
        return SuccessResponse.of(result)
    }

    @PostMapping("/bulk-ingester")
    fun bulkIngester(): SuccessResponse<String> {
        val result = esJavaClientService.bulkWithIngester()
        return SuccessResponse.DEFAULT
    }
}
