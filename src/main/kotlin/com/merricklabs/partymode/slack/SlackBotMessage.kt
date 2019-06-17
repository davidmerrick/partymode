package com.merricklabs.partymode.slack

// Represents a message to be posted by our bot
data class SlackBotMessage(val channel: String, val text: String)