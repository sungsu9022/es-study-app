package com.starter.user.app.user.model

import com.starter.core.common.utils.JsonUtil
import io.kotest.core.spec.style.AnnotationSpec
import mu.KotlinLogging

internal class UserCreateRequestTest : AnnotationSpec() {
    companion object {
        private val logger = KotlinLogging.logger {}
    }

    @Test
    fun getJson() {
        val userCreateRequest = UserCreateRequest(
            email = "sungsu9022@naver.com",
            userName = "sungsu",
        )

        logger.info { JsonUtil.toJson(userCreateRequest) }
    }
}
