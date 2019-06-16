package com.merricklabs.partymode.twilio

import com.merricklabs.partymode.twilio.TwilioFields.CALLER
import com.merricklabs.partymode.twilio.TwilioFields.CALL_SID
import com.merricklabs.partymode.twilio.TwilioFields.DIGITS
import com.merricklabs.partymode.twilio.TwilioFields.FROM
import com.merricklabs.partymode.twilio.TwilioFields.TO
import java.net.URLDecoder

data class TwilioParams(private val paramMap: Map<String, String>) {

    constructor(rawParams: String) : this(parseParams(rawParams))

    fun isValidPayload() = validationFields.all { paramMap.contains(it) }
    fun validationParams() = paramMap.filter { validationFields.contains(it.key) || it.key == DIGITS }
    fun from(): String? = paramMap[FROM]

    private companion object {
        val validationFields = listOf(CALL_SID, CALLER, FROM, TO)
        fun parseParams(rawParams: String): Map<String, String> {
            // Twilio POSTS an application/x-www-form-urlencoded string to this endpoint.
            // Build a map with it.
            return URLDecoder.decode(rawParams, "UTF-8")
                    .split("&")
                    .map { it.split("=") }
                    .map { it[0] to it[1] }
                    .toMap()
        }
    }
}