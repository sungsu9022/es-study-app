package com.starter.dbmigration

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.context.properties.ConfigurationPropertiesScan
import org.springframework.boot.runApplication


@ConfigurationPropertiesScan
@SpringBootApplication(
    scanBasePackages = ["com.starter.dbmigration", "com.starter.core"],
)
class DbMigrationApplication

fun main(args: Array<String>) {
    runApplication<DbMigrationApplication>(*args)
}
