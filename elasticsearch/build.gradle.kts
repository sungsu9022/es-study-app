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
    val esVersion = "8.11.1"

    implementation(project(":core"))
    testImplementation(project(":core"))

    // es 저수준 rest Client
    implementation("org.elasticsearch.client:elasticsearch-rest-client:$esVersion")

    testImplementation("org.springframework.boot:spring-boot-starter-test")
}
