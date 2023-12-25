package com.starter.core.config.jasypt

import com.starter.core.jasypt.config.JasyptConfig
import io.kotest.core.spec.style.AnnotationSpec
import io.kotest.matchers.shouldBe
import mu.KotlinLogging

internal class JasyptConfigTest : AnnotationSpec() {
    companion object {
        private val logger = KotlinLogging.logger {}
    }

    @Test
    fun encryptDecryptTest() {
        val testValue = "test"
        val encryptor = JasyptConfig.create("testPassword")
        val encrypt = encryptor.encrypt(testValue)
        logger.info { "encrypt : $encrypt" }
        encryptor.decrypt("Bm/DTwdBcMahJLRGzHjqRA==") shouldBe testValue
    }
}
