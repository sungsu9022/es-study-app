package com.starter.admin.app.home

import org.springframework.http.MediaType
import org.springframework.stereotype.Controller
import org.springframework.web.bind.annotation.GetMapping

@Controller
class HomeRestController {

    @GetMapping(value = ["/", "/index"], produces = [MediaType.TEXT_HTML_VALUE])
    fun getHome(): String {
        return "home"
    }
}

