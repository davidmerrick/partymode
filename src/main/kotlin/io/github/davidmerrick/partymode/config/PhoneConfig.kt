package io.github.davidmerrick.partymode.config

import javax.validation.constraints.NotBlank

class PhoneConfig {
    @get:NotBlank
    lateinit var myNumber: String

    @get:NotBlank
    lateinit var callboxNumber: String
}