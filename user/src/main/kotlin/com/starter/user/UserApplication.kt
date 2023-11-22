package com.starter.user

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication

@SpringBootApplication(
    scanBasePackages = ["com.starter.user", "com.starter.core"],
)
class UserApplication

fun main(args: Array<String>) {
    runApplication<UserApplication>(*args)
}
