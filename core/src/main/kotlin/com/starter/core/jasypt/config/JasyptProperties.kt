package com.starter.core.jasypt.config

import com.starter.core.common.config.Profiles
import org.springframework.boot.context.properties.ConfigurationProperties
import org.springframework.context.annotation.Profile

@Profile(Profiles.JASYPT)
@ConfigurationProperties("jasypt.encryptor")
data class JasyptProperties(
    val password: String
)
