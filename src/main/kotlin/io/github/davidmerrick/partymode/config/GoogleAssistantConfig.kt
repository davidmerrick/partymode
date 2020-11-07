package io.github.davidmerrick.partymode.config

import io.micronaut.context.annotation.ConfigurationProperties
import javax.validation.constraints.NotBlank

@ConfigurationProperties("googleAssistant")
class GoogleAssistantConfig {
    @get:NotBlank
    lateinit var appName: String
}
