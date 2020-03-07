package com.merricklabs.partymode.bots

import com.merricklabs.partymode.slack.SlackBotMessage
import com.merricklabs.partymode.slack.SlackCallbackMessage
import com.merricklabs.partymode.storage.PartymodeStorage
import mu.KotlinLogging
import org.koin.core.inject

private val log = KotlinLogging.logger {}

private val HELP_TEXT = """
Usage: 
* `pm [1-5]`: Enable partymode for n hours
* `pm disable`: Disable partymode
""".trimIndent()

class PartyBot : SlackBot() {
    private val storage: PartymodeStorage by inject()

    override fun handle(message: SlackCallbackMessage): SlackBotMessage {
        log.info("Handling message")
        operator fun Regex.contains(text: CharSequence): Boolean = this.matches(text)

        return when (val text = message.event.text.toLowerCase()) {
            in Regex(".*pm help.*") -> constructReply(message, HELP_TEXT)
            in Regex(".*pm [1-5]$") -> {
                val regex = "[1-5]$".toRegex()
                val numHours = regex.find(text)!!.value.toInt()
                storage.enableForHours(numHours)
                val suffix = if (numHours > 1) "hours" else "hour"
                constructReply(message, "partymode enabled for $numHours $suffix")
            }
            in Regex(".*pm disable.*") -> {
                storage.disablePartyMode()
                constructReply(message, "partymode disabled")
            }
            in Regex(".*pm status.*") -> {
                val status = if (storage.isPartymodeEnabled()) {
                    "enabled"
                } else {
                    "disabled"
                }
                constructReply(message, "partymode $status")
            }
            else -> constructReply(message, HELP_TEXT)
        }
    }

    private fun constructReply(originalMessage: SlackCallbackMessage, text: String): SlackBotMessage {
        return SlackBotMessage(originalMessage.event.channel, "<@${originalMessage.event.user}>: $text")
    }
}