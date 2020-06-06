package io.github.davidmerrick.partymode.config

class PhoneConfig {
    val myNumber: String = System.getenv("MY_NUMBER")
    val callboxNumber: String = System.getenv("CALLBOX_NUMBER")
}