package io.github.davidmerrick.partymode.external.slack

import io.github.davidmerrick.slakson.messages.EventCallbackMessage
import io.github.davidmerrick.slakson.messages.SlackMessage
import javax.inject.Singleton

@Singleton
class MessageFilter {

    /**
     * Boolean indicating whether the message was filtered
     */
    fun apply(message: SlackMessage): Boolean {
        return when (message) {
            is EventCallbackMessage -> !message.isBotMessage()
            else -> true
        }
    }
}