package io.github.davidmerrick.partymode.config

import io.micronaut.context.annotation.ConfigurationProperties
import javax.validation.constraints.NotBlank

@ConfigurationProperties("partymode")
class PartymodeConfig {
    @get:NotBlank
    lateinit var botName: String

    @ConfigurationProperties("firestore")
    class FirestoreConfig {
        @get:NotBlank
        lateinit var projectId: String

        @get:NotBlank
        lateinit var collectionName: String
    }

    @ConfigurationProperties("phone")
    class PhoneConfig {
        @get:NotBlank
        lateinit var myNumber: String

        @get:NotBlank
        lateinit var callboxNumber: String
    }
}

