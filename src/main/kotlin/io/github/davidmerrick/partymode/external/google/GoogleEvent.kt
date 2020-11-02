package io.github.davidmerrick.partymode.external.google

import com.fasterxml.jackson.annotation.JsonIgnoreProperties

@JsonIgnoreProperties(ignoreUnknown = true)
data class GoogleEvent(
    val handler: HandlerField,
    val session: GoogleSession
)

@JsonIgnoreProperties(ignoreUnknown = true)
data class HandlerField(val name: String)

@JsonIgnoreProperties(ignoreUnknown = true)
data class GoogleSession(
    val id: String
)
