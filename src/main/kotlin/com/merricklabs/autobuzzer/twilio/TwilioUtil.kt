package com.merricklabs.autobuzzer.twilio

import java.net.URLDecoder

object TwilioUtil {
    fun parseInput(input: String): Map<String, List<String>> {
        val decoded = URLDecoder.decode(input, "UTF-8")
        return decoded
                .split("&")
                .asSequence()
                .map { it.split("=") }
                .map { mapOf(it[0] to it[1]) }
                .flatMap { it.asSequence() }
                .groupBy({it.key}, {it.value})
    }
}