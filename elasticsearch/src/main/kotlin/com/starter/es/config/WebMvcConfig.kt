package com.starter.es.config

import com.fasterxml.jackson.databind.ObjectMapper
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.format.FormatterRegistry
import org.springframework.format.datetime.standard.DateTimeFormatterRegistrar
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer
import java.time.format.DateTimeFormatter

@Configuration
class WebMvcConfig(
    private val objectMapper: ObjectMapper,
) : WebMvcConfigurer {

    @Bean
    fun mappingJackson2HttpMessageConverter(): MappingJackson2HttpMessageConverter {
        return MappingJackson2HttpMessageConverter(objectMapper)
    }

    override fun addFormatters(formatterRegistry: FormatterRegistry) {
        val dateTimeFormatterRegistrar = DateTimeFormatterRegistrar()

        dateTimeFormatterRegistrar.setDateFormatter(DateTimeFormatter.ISO_DATE)
        dateTimeFormatterRegistrar.setDateTimeFormatter(DateTimeFormatter.ISO_DATE_TIME)
        dateTimeFormatterRegistrar.registerFormatters(formatterRegistry)
    }
}
