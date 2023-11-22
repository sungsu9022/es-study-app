package com.starter.user.app

import com.starter.core.common.response.ServiceResponse
import com.starter.core.common.response.SuccessResponse
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RestController

@RestController
class HomeRestController {

    @GetMapping("/api/home")
    fun getHome(): ServiceResponse {
        return SuccessResponse.of(HomeResult("home"))
    }
}

class HomeResult(val message: String)
