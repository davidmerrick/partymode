package com.merricklabs.partymode.bots

import com.merricklabs.partymode.config.PartymodeConfig
import com.merricklabs.partymode.slack.SlackBotMessage
import com.merricklabs.partymode.slack.SlackCallbackMessage
import org.koin.core.KoinComponent
import org.koin.core.inject

const val SLACK_URL = "https://slack.com/api/chat.postMessage"

abstract class SlackBot : KoinComponent {
    protected val config: PartymodeConfig by inject()

    fun shouldHandle(message: SlackCallbackMessage) = message.event.type == "app_mention"
        || (message.event.channel_type == "im" && message.event.subtype != "bot_message")
        || message.event.text.contains(config.slack.botName)

    abstract fun handle(message: SlackCallbackMessage): SlackBotMessage
}