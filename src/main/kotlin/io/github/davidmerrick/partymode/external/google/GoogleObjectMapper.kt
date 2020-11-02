package io.github.davidmerrick.partymode.external.google

import com.fasterxml.jackson.databind.ObjectMapper
import com.fasterxml.jackson.module.kotlin.registerKotlinModule
import javax.inject.Singleton

@Singleton
class GoogleObjectMapper : ObjectMapper() {
    init {
        registerKotlinModule()
    }
}
