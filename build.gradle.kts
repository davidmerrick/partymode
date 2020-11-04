group = "io.github.davidmerrick.partymode"

repositories {
    mavenCentral()
    jcenter()
}

plugins {
    id("com.github.johnrengelman.shadow") version "5.2.0"
    kotlin("jvm") version "1.4.10"
    kotlin("kapt") version "1.4.10"
    kotlin("plugin.allopen") version "1.4.10"
    application
}

application {
    mainClassName = "io.github.davidmerrick.partymode.Application"
}

// Compiler plugin which makes classes with the following
// annotations open
allOpen {
    annotations(
        "io.micronaut.aop.Around",
        "io.micronaut.http.annotation.Controller",
        "javax.inject.Singleton"
    )
}

dependencies {
    val micronautVersion by extra("2.1.2")
    val awsSdkVersion by extra("2.13.31")
  val googleSdkVersion by extra("2.1.0")

    kapt(platform("io.micronaut:micronaut-bom:$micronautVersion"))
    kapt("io.micronaut:micronaut-inject-java")
    kapt("io.micronaut:micronaut-validation")
    kapt("io.micronaut.security:micronaut-security")

    implementation(platform("io.micronaut:micronaut-bom:$micronautVersion"))
    implementation("io.micronaut:micronaut-runtime")
    implementation("io.micronaut:micronaut-inject")
    implementation("io.micronaut:micronaut-validation")
    implementation("io.micronaut.security:micronaut-security:$micronautVersion")
    implementation("io.micronaut.security:micronaut-security-jwt:$micronautVersion")
    implementation("io.micronaut.gcp:micronaut-gcp-tracing:3.2.1")
    implementation("com.fasterxml.jackson.module:jackson-module-kotlin")
    implementation("io.github.microutils:kotlin-logging:1.7.2")
    implementation("org.slf4j:slf4j-simple:1.8.0-beta4")
    implementation("io.micronaut:micronaut-http-server-netty")
    implementation("io.github.davidmerrick.slakson:slakson:2.0.2")
    implementation("io.micronaut:micronaut-management")
  implementation("io.micronaut:micronaut-http-client")
  implementation("com.google.cloud:google-cloud-firestore:$googleSdkVersion")
  implementation("com.twilio.sdk:twilio:7.39.0")
    implementation("software.amazon.awssdk:sns:$awsSdkVersion")

    // Test

    kaptTest(platform("io.micronaut:micronaut-bom:$micronautVersion"))
    kaptTest("io.micronaut:micronaut-inject-java")

    testImplementation("org.spekframework.spek2:spek-runner-junit5:2.0.8")
    testImplementation("org.junit.jupiter:junit-jupiter-api:5.6.2")
    testImplementation("org.junit.jupiter:junit-jupiter-engine:5.6.2")
    testImplementation("io.kotlintest:kotlintest-runner-junit5:3.3.2")
    testImplementation("io.micronaut.test:micronaut-test-spock")
    testImplementation("io.micronaut.test:micronaut-test-kotlintest")
    testImplementation("io.micronaut.test:micronaut-test-junit5")
    testImplementation("io.mockk:mockk:1.10.0")
}

tasks {
    compileKotlin {
        kotlinOptions {
            jvmTarget = "11"
            javaParameters = true
        }
    }

    compileTestKotlin {
        kotlinOptions {
            jvmTarget = "11"
            javaParameters = true
        }
    }

    test {
        useJUnitPlatform()
    }

    named<JavaExec>("run") {
        doFirst {
            jvmArgs = listOf("-noverify", "-XX:TieredStopAtLevel=1", "-Dcom.sun.management.jmxremote")
        }
    }

    shadowJar {
        archiveBaseName.set("application")
        archiveClassifier.set("")
        archiveVersion.set("")
        mergeServiceFiles()
        transform(com.github.jengelman.gradle.plugins.shadow.transformers.Log4j2PluginsCacheFileTransformer::class.java)
    }

    withType<JavaCompile> {
        options.encoding = "UTF-8"
    }

    withType<Jar> {
        manifest {
            attributes["Main-Class"] = application.mainClassName
        }
    }
}
