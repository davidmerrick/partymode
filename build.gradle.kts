group = "com.merricklabs.partymode"
version = "dev"

buildscript {
    repositories {
        mavenCentral()
        jcenter()
    }
    dependencies {
        classpath(kotlin("gradle-plugin", version = "1.3.31"))
        classpath("de.sebastianboegl.gradle.plugins:shadow-log4j-transformer:2.1.1")
        classpath("com.github.jengelman.gradle.plugins:shadow:2.0.1")
    }
}

repositories {
    mavenCentral()
    jcenter()
}

plugins {
    java
    kotlin("jvm") version "1.3.31"
    id("com.github.johnrengelman.shadow") version "4.0.4"
}

dependencies {
    implementation("com.twilio.sdk:twilio:7.17.0")
    implementation("org.jetbrains.kotlin:kotlin-stdlib-jdk8")
    implementation("org.slf4j:slf4j-api:1.7.25")
    implementation("org.slf4j:slf4j-jdk14:1.7.26")
    implementation("io.github.microutils:kotlin-logging:1.6.10")
    implementation("org.koin:koin-core:1.0.1")
    implementation("com.amazonaws:aws-lambda-java-core:1.1.0")
    implementation("com.amazonaws:aws-lambda-java-log4j2:1.0.0")
    implementation("com.amazonaws:aws-lambda-java-events:2.0.1")
    implementation("com.amazonaws:aws-java-sdk-dynamodb:1.11.527")
    implementation("com.fasterxml.jackson.core:jackson-core:2.8.5")
    implementation("com.fasterxml.jackson.core:jackson-databind:2.8.5")
    implementation("com.fasterxml.jackson.core:jackson-annotations:2.8.5")

    testImplementation("org.testng:testng:6.14.3")
    testImplementation("org.koin:koin-test:1.0.1")
}

val deploy = tasks.create<Exec>("deploy") {
    commandLine = listOf("serverless", "deploy")
}

deploy.dependsOn(tasks.getByName("shadowJar"))