package com.starter.core.security.config

import com.starter.core.common.config.Profiles
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.context.annotation.Profile
import org.springframework.http.HttpMethod
import org.springframework.security.config.annotation.web.builders.HttpSecurity
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity
import org.springframework.security.web.SecurityFilterChain
import org.springframework.security.web.header.writers.ReferrerPolicyHeaderWriter
import org.springframework.web.cors.CorsConfiguration
import org.springframework.web.cors.CorsConfigurationSource
import org.springframework.web.cors.UrlBasedCorsConfigurationSource
import java.time.Duration

@Profile(Profiles.SECURITY)
@Configuration
@EnableWebSecurity
class SecurityConfig(private val securityProperties: SecurityProperties) {
    companion object {
        val CORS_ALLOWED_METHODS = listOf(
            HttpMethod.GET.toString(),
            HttpMethod.POST.toString(),
            HttpMethod.PATCH.toString(),
            HttpMethod.PUT.toString(),
            HttpMethod.DELETE.toString(),
            HttpMethod.OPTIONS.toString(),
        )
    }

    @Bean
    fun filterChain(http: HttpSecurity): SecurityFilterChain {
        http.cors().configurationSource(corsConfigurationSource())
            .and()
            .csrf().disable()
            .formLogin().disable()
        http.headers().defaultsDisabled()
            .cacheControl()
            .and()
            .httpStrictTransportSecurity()
            .includeSubDomains(true)
            .maxAgeInSeconds(Duration.ofDays(365).seconds)
            .and()
            .referrerPolicy(ReferrerPolicyHeaderWriter.ReferrerPolicy.STRICT_ORIGIN)
            .and()
            .contentTypeOptions()

        return http.build()
    }

// 	fun configure(web: WebSecurity) {
// 		val ignoreAntMatchers: MutableList<String> = ArrayList()
// 		val swaggerUIPath: String = securityProperty.swaggerUIPath
// 		if (StringUtils.isNotBlank(swaggerUIPath)) {
// 			ignoreAntMatchers.add(swaggerUIPath)
// 		}
//
// 		web.ignoring()
// 			.antMatchers(*ignoreAntMatchers.toTypedArray())
// 	}

    private fun corsConfigurationSource(): CorsConfigurationSource {
        val corsConfiguration = CorsConfiguration()
        corsConfiguration.allowedOrigins = listOf(CorsConfiguration.ALL)
        corsConfiguration.allowedHeaders = listOf(CorsConfiguration.ALL)
        CORS_ALLOWED_METHODS.forEach { method -> corsConfiguration.addAllowedMethod(method) }
        corsConfiguration.allowCredentials = true
        corsConfiguration.setMaxAge(Duration.ofDays(1))
        val urlBasedCorsConfigurationSource = UrlBasedCorsConfigurationSource()
        urlBasedCorsConfigurationSource.registerCorsConfiguration("/**", corsConfiguration)
        return urlBasedCorsConfigurationSource
    }
}
