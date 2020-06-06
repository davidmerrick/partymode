package io.github.davidmerrick.partymode.config

class TwilioConfig {
    val authToken: String = System.getenv("TWILIO_AUTH_TOKEN")
}