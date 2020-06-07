package io.github.davidmerrick.partymode.config

import javax.validation.constraints.NotBlank

class DynamoDbConfig {
    @get:NotBlank
    lateinit var endpoint: String

    @get:NotBlank
    lateinit var region: String

    @get:NotBlank
    lateinit var tableName: String
}