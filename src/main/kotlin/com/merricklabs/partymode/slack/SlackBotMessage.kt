package com.merricklabs.echobot.slack

// Represents a message to be posted by our bot
data class SlackBotMessage(val channel: String, val text: String)