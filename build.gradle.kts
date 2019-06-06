group = "com.merricklabs.partymode"

buildscript {
    repositories {
        mavenCentral()
        jcenter()
    }
    dependencies {
        classpath(kotlin("gradle-plugin", version = Versions.kotlin))
        classpath("de.sebastianboegl.gradle.plugins:shadow-log4j-transformer:2.1.1")
        classpath("com.github.jengelman.gradle.plugins:shadow:${Versions.shadow}")
    }
}

repositories {
    mavenCentral()
    jcenter()
}

plugins {
    java
    `kotlin-dsl` // Enables experimental Kotlin compiler features
    kotlin("jvm") version Versions.kotlin
    id("com.github.johnrengelman.shadow") version Versions.shadow apply false
}

// Disable warnings about experimental features
kotlinDslPluginOptions {
    experimentalWarning.set(false)
}

dependencies {
    implementation("com.twilio.sdk:twilio:7.17.0")
    implementation("com.squareup.okhttp3:okhttp:3.14.1")
    implementation("org.jetbrains.kotlin:kotlin-stdlib-jdk8")
    implementation("org.slf4j:slf4j-api:1.7.25")
    implementation("org.slf4j:slf4j-jdk14:1.7.26")
    implementation("io.github.microutils:kotlin-logging:1.6.10")
    implementation("org.koin:koin-core:${Versions.koin}")
    implementation("com.amazonaws:aws-lambda-java-core:1.1.0")
    implementation("com.amazonaws:aws-lambda-java-log4j2:1.0.0")
    implementation("com.amazonaws:aws-lambda-java-events:2.0.1")
    implementation("com.amazonaws:aws-java-sdk-dynamodb:${Versions.awsSdk}")
    implementation("com.amazonaws:aws-java-sdk-sns:${Versions.awsSdk}")
    implementation("com.fasterxml.jackson.core:jackson-core:${Versions.jackson}")
    implementation("com.fasterxml.jackson.core:jackson-databind:${Versions.jackson}")
    implementation("com.fasterxml.jackson.core:jackson-annotations:${Versions.jackson}")
    implementation("com.fasterxml.jackson.module:jackson-module-kotlin:${Versions.jackson}")

    testImplementation("org.testng:testng:6.14.3")
    testImplementation("org.koin:koin-test:1.0.1")
}

val deployDev = tasks.create<Exec>("deployDev") {
    commandLine = listOf("serverless", "deploy", "--stage=dev")
}

val deployPrd = tasks.create<Exec>("deployPrd") {
    commandLine = listOf("serverless", "deploy", "--stage=prd")
}

deployDev.dependsOn(tasks.getByName("shadowJar"))
deployPrd.dependsOn(tasks.getByName("shadowJar"))