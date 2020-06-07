package io.github.davidmerrick.partymode.config

import io.micronaut.context.annotation.ConfigurationProperties
import java.time.LocalDate
import javax.inject.Singleton
import javax.validation.Valid
import javax.validation.constraints.NotBlank
import javax.validation.constraints.NotNull

@ConfigurationProperties("partymode")
@Singleton
class PartymodeConfig {
    @get:NotBlank
    lateinit var quarantineDate: LocalDate

    @get:NotBlank
    lateinit var botName: String

    @get:Valid
    @get:NotNull
    lateinit var dynamo: DynamoDbConfig

    @get:Valid
    @get:NotNull
    lateinit var phone: PhoneConfig

    @get:NotNull
    lateinit var sns: SnsConfig

    @get:NotNull
    lateinit var twilio: TwilioConfig
}

