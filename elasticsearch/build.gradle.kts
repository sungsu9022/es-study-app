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
    val esHighLevelClientVersion = "7.17.16"

    implementation(project(":core"))
    testImplementation(project(":core"))

    // es 저수준 rest client
    implementation("org.elasticsearch.client:elasticsearch-rest-client:$esVersion")
    // es 고수준 rest client
    implementation("org.elasticsearch.client:elasticsearch-rest-high-level-client:$esHighLevelClientVersion")
    // 수동 jar 설치 후 적용하는 방식
//    implementation(fileTree(mapOf("dir" to "manual-build", "includes" to listOf("*.jar"))))
//    implementation("org.elasticsearch:elasticsearch:$esVersion")

    // java client
    implementation("co.elastic.clients:elasticsearch-java:$esVersion")

    testImplementation("org.springframework.boot:spring-boot-starter-test")
}
