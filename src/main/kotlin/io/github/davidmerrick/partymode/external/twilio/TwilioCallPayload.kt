package io.github.davidmerrick.partymode.external.twilio

import io.github.davidmerrick.partymode.external.twilio.TwilioFields.FROM
import java.net.URLDecoder

data class TwilioCallPayload(val paramMap: Map<String, String>) {

    constructor(rawParams: String) : this(parseParams(rawParams))

    fun from(): String? = paramMap[FROM]

    private companion object {
        fun parseParams(rawParams: String): Map<String, String> {
            return URLDecoder.decode(rawParams, "UTF-8")
                    .split("&")
                    .map { it.split("=") }
                    .map { it[0] to it[1] }
                    .toMap()
        }
    }
}