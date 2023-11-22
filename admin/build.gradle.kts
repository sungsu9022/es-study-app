import com.github.gradle.node.yarn.task.YarnTask

repositories {
    gradlePluginPortal()
}

plugins {
    id("com.github.node-gradle.node") version "3.5.1"
}

dependencies {
    implementation(project(":core"))
    testImplementation(project(":core"))

    implementation("org.springframework.boot:spring-boot-starter-thymeleaf")
    implementation("com.github.node-gradle:gradle-node-plugin:3.5.1")

    runtimeOnly("org.springframework.boot:spring-boot-devtools")
}

plugins.apply("com.github.node-gradle.node")

tasks {
    application {
        mainClass.set("com.starter.admin.AdminApplicationKt")
    }

    bootJar {
        enabled = true
        dependsOn(deleteDist)
    }

    jar {
        enabled = false
    }

    node {
        version.set("16.15.0")
        yarnVersion.set("1.22.19")
        download.set(true)
    }
}
val yarnInstall = tasks.register<YarnTask>("yarnInstall") {
    args.set(listOf("install"))
}

val yarnBuild = tasks.register<YarnTask>("yarnBuild") {
    dependsOn(yarnInstall)
    args.set(listOf("run", "build:product"))
}

val copyDist= tasks.register<Copy>("copyDist") {
    dependsOn(yarnBuild)
    from("build/prepare") {
        include("**/*")
    }
    into("build/resources/main")
    includeEmptyDirs = true
}

val deleteDist=  tasks.register<Delete>("deleteDist") {
    dependsOn(copyDist)
    delete("build/prepare")
}
