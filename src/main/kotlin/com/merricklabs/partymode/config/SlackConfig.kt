package com.merricklabs.partymode.config

class SlackConfig {
    val botToken: String = System.getenv("BOT_TOKEN")
    val botName = System.getenv("BOT_NAME") ?: "partybot"
}