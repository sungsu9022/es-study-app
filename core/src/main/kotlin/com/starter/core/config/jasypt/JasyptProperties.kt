package com.starter.core.config.jasypt

import com.starter.core.config.Profiles
import org.springframework.boot.context.properties.ConfigurationProperties
import org.springframework.context.annotation.Profile

@Profile(Profiles.JASYPT)
@ConfigurationProperties("jasypt.encryptor")
data class JasyptProperties(
    val password: String
)
