tasks {
    application {
        mainClass.set("com.starter.dbmigration.DbMigrationApplicationKt")
    }

    bootJar {
        enabled = true
    }

    jar {
        enabled = false
    }
}

dependencies {
    val flywayVersion = "9.21.1"

    implementation(project(":core"))
    testImplementation(project(":core"))

    // flyway
    implementation("org.flywaydb:flyway-core:$flywayVersion")
    implementation("org.flywaydb:flyway-mysql:$flywayVersion")
}
