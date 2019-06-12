package com.merricklabs.partymode.config

class SlackConfig {
    val webhookUri: String = System.getenv("SLACK_WEBHOOK_URI")
    val botToken: String = System.getenv("BOT_TOKEN")
    val botName = System.getenv("BOT_NAME") ?: "partybot"
}