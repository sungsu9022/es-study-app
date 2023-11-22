import org.jetbrains.kotlin.gradle.tasks.KotlinCompile
import org.jlleitschuh.gradle.ktlint.tasks.KtLintCheckTask

plugins {
    val kotlinVersion = "1.8.22"
    val springBootVersion = "3.1.2"
    val ktLintVersion = "11.3.1"

    id("org.springframework.boot") version springBootVersion
    id("io.spring.dependency-management") version "1.1.0"

    id("com.adarshr.test-logger") version "3.2.0"
    id("org.jetbrains.kotlin.plugin.allopen") version kotlinVersion
    id("org.jetbrains.kotlin.plugin.noarg") version kotlinVersion
    id("org.jlleitschuh.gradle.ktlint") version ktLintVersion
    id("org.jlleitschuh.gradle.ktlint-idea") version ktLintVersion

    kotlin("jvm") version kotlinVersion
    kotlin("plugin.spring") version kotlinVersion
    kotlin("plugin.jpa") version kotlinVersion
    kotlin("kapt") version kotlinVersion
}

java.sourceCompatibility = JavaVersion.VERSION_17

allOpen {
    annotation("com.starter.core.common.annotations.AllOpen")
}

allprojects {
    group = "com.starter"
    version = "1.0.0-SNAPSHOT"

    repositories {
        mavenCentral()
        google()
        maven("https://repo.spring.io/snapshot")
        maven("https://repo.spring.io/milestone")
        maven("https://plugins.gradle.org/m2/")
    }

    tasks.withType<KotlinCompile> {
        kotlinOptions {
            freeCompilerArgs = listOf("-Xjsr305=strict", "-Xjvm-default=all")
            jvmTarget = "17"
        }
    }

    tasks.withType<Test> {
        useJUnitPlatform()
    }

    tasks.withType<KtLintCheckTask> {
        exclude { element -> element.file.path.contains("test") }
    }
}

subprojects {
    apply {
        plugin("java")
        plugin("kotlin")
        plugin("kotlin-jpa")
        plugin("kotlin-kapt")
        plugin("kotlin-spring")
        plugin("kotlin-allopen")
        plugin("kotlin-noarg")
        plugin("org.springframework.boot")
        plugin("io.spring.dependency-management")
    }

    if (this.name == "core") {
        apply {
            plugin("java-library")
        }
    } else {
        apply {
            plugin("application")
        }
    }

    val mapstructVersion = "1.5.3.Final"
    val springDocVersion = "2.0.3"
    val caffeineVersion = "3.1.5"
    val kotlinLoggingVersion = "3.0.5"
    val apacheCommonsVersion = "2.11.0"
    val kotestVersion = "5.5.5"
    val mockkVersion = "1.13.4"
    val jacksonVersion = "2.14.2"
    val jettyHttpClientVersion = "1.1.13"
    val querydslVersion = "5.0.0"
    val hibernateUtilsVersion = "3.2.0"
    val mysqlConnectorVersion = "8.0.32"
    val coroutineVersion = "1.6.4"
    val jasyptVersion = "3.0.5"

    dependencyManagement {
        imports {
            mavenBom("com.fasterxml.jackson:jackson-bom:$jacksonVersion")
        }

        dependencies {
            dependency("org.mapstruct:mapstruct:$mapstructVersion")
            dependency("org.mapstruct:mapstruct-processor:$mapstructVersion")
            dependency("com.fasterxml.jackson.datatype:jackson-datatype-jsr310:$jacksonVersion")
        }
    }

    dependencies {
        // Spring BOot, Kotlin
        implementation("org.springframework.boot:spring-boot-starter-web")
        implementation("org.springframework.boot:spring-boot-starter-validation")
        implementation("org.springframework.boot:spring-boot-starter-cache")
        implementation("org.springframework.boot:spring-boot-starter-webflux")
        implementation("org.springframework.boot:spring-boot-starter-security")
        implementation("com.fasterxml.jackson.module:jackson-module-kotlin")
        implementation("org.jetbrains.kotlin:kotlin-reflect")
        implementation("org.jetbrains.kotlin:kotlin-stdlib-jdk8")
        kapt("org.springframework.boot:spring-boot-configuration-processor")

        // spring doc
        implementation("org.springdoc:springdoc-openapi-starter-webmvc-ui:$springDocVersion")

        // DB
        implementation("org.springframework.boot:spring-boot-starter-data-jpa")
        implementation("com.mysql:mysql-connector-j:$mysqlConnectorVersion")
        implementation("io.hypersistence:hypersistence-utils-hibernate-60:$hibernateUtilsVersion")
        implementation("com.querydsl:querydsl-core:$querydslVersion")
        implementation("com.querydsl:querydsl-jpa:$querydslVersion:jakarta")
        kapt("com.querydsl:querydsl-apt:$querydslVersion:jakarta")

        // mapstruct
        implementation("org.mapstruct:mapstruct")
        implementation("org.mapstruct:mapstruct-processor")
        kapt("org.mapstruct:mapstruct-processor")
        kaptTest("org.mapstruct:mapstruct-processor")

        // 기타 라이브러리
        implementation("commons-io:commons-io:$apacheCommonsVersion")
        implementation("io.github.microutils:kotlin-logging:$kotlinLoggingVersion")
        implementation("com.github.ben-manes.caffeine:caffeine:$caffeineVersion")
        implementation("org.eclipse.jetty:jetty-reactive-httpclient:$jettyHttpClientVersion")
        implementation("org.jetbrains.kotlinx:kotlinx-coroutines-core:$coroutineVersion")
        runtimeOnly("org.jetbrains.kotlinx:kotlinx-coroutines-core-jvm:$coroutineVersion")

        implementation("com.github.ulisesbocchio:jasypt-spring-boot:$jasyptVersion")

        // test
        testImplementation("org.springframework.boot:spring-boot-starter-test")
        testImplementation("io.mockk:mockk:$mockkVersion")
        testImplementation("io.kotest:kotest-runner-junit5:$kotestVersion")
        testImplementation("io.kotest:kotest-assertions-core:$kotestVersion")
        testImplementation("io.kotest.extensions:kotest-extensions-spring:1.1.2")
        testImplementation("com.h2database:h2:1.3.176")
    }

    kapt {
        correctErrorTypes = true
        arguments {
            /**
             * @Mapper(componentModel = "spring")를 기본값으로 설정
             * @Mapper 어노테이션이 붙은 xxxMapperImpl을 스프링 빈으로 만듬
             */
            arg("mapstruct.defaultComponentModel", "spring")
            arg("mapstruct.defaultInjectionStrategy", "constructor")
        }
    }

    noArg {
        annotation("jakarta.persistence.Entity")
        annotation("jakarta.persistence.MappedSuperclass")
        annotation("jakarta.persistence.Embeddable")
        annotation("com.starter.core.common.annotations.NoArg")
        invokeInitializers = true
    }

    allOpen {
        annotation("jakarta.persistence.Entity")
        annotation("jakarta.persistence.MappedSuperclass")
        annotation("jakarta.persistence.Embeddable")
        annotation("com.starter.core.common.annotations.AllOpen")
    }
}

tasks {
    bootJar { enabled = false }
    jar { enabled = false }
}
