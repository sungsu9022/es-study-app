package com.starter.es

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration
import org.springframework.boot.autoconfigure.jdbc.DataSourceTransactionManagerAutoConfiguration
import org.springframework.boot.autoconfigure.orm.jpa.HibernateJpaAutoConfiguration
import org.springframework.boot.runApplication

@SpringBootApplication(
    scanBasePackages = [
        "com.starter.es",
        "com.starter.core.common",
        "com.starter.core.jasypt",
        "com.starter.core.security",
    ],
    exclude = [
        DataSourceAutoConfiguration::class,
        DataSourceTransactionManagerAutoConfiguration::class,
        HibernateJpaAutoConfiguration::class,
    ],
)
class EsApplication

fun main(args: Array<String>) {
    runApplication<EsApplication>(*args)
}
