package com.starter.core.config.security

import com.starter.core.config.Profiles
import org.springframework.boot.context.properties.ConfigurationProperties
import org.springframework.context.annotation.Profile

@ConfigurationProperties(prefix = "starter.security")
@Profile(Profiles.SECURITY)
data class SecurityProperties(
    val swaggerUIPath: String
)
