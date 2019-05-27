package com.merricklabs.partymode.util

import com.fasterxml.jackson.databind.ObjectMapper
import com.fasterxml.jackson.module.kotlin.KotlinModule

class PartymodeObjectMapper : ObjectMapper() {
    init {
        this.registerModule(KotlinModule())
    }
}