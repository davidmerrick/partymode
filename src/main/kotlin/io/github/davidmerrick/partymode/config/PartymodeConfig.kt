package io.github.davidmerrick.partymode.config

import io.micronaut.context.annotation.ConfigurationProperties
import javax.validation.constraints.NotBlank

@ConfigurationProperties("partymode")
class PartymodeConfig {
    @get:NotBlank
    lateinit var botName: String

    @ConfigurationProperties("dynamo")
    class DynamoConfig {
        @get:NotBlank
        lateinit var endpoint: String

        @get:NotBlank
        lateinit var region: String

        @get:NotBlank
        lateinit var tableName: String
    }

    @ConfigurationProperties("twilio")
    class TwilioConfig {
        @get:NotBlank
        lateinit var authToken: String
    }

    @ConfigurationProperties("phone")
    class PhoneConfig {
        @get:NotBlank
        lateinit var myNumber: String

        @get:NotBlank
        lateinit var callboxNumber: String
    }

    @ConfigurationProperties("sns")
    class SnsConfig {
        var topicArn: String? = null
        var enabled: Boolean = false
    }
}

