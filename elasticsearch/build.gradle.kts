tasks {
    application {
        mainClass.set("com.starter.es.EsApplicationKt")
    }

    bootJar {
        enabled = true
    }

    jar {
        enabled = false
    }
}

dependencies {
    implementation(project(":core"))
    testImplementation(project(":core"))

    testImplementation("org.springframework.boot:spring-boot-starter-test")
}
