package io.github.davidmerrick.partymode.bots

import io.github.davidmerrick.partymode.storage.PartymodeStorage
import io.github.davidmerrick.slakson.messages.CreateMessagePayload
import io.github.davidmerrick.slakson.messages.EventCallbackMessage
import mu.KotlinLogging
import javax.inject.Singleton

private val log = KotlinLogging.logger {}

private val HELP_TEXT = """
Usage: 
* `pm [1-5]`: Enable partymode for n hours
* `pm disable`: Disable partymode
""".trimIndent()

@Singleton
class PartyBot(private val storage: PartymodeStorage) {

    fun handle(message: EventCallbackMessage): CreateMessagePayload {
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

    private fun constructReply(originalMessage: EventCallbackMessage, text: String): CreateMessagePayload {
        return CreateMessagePayload(originalMessage.event.channel, "<@${originalMessage.event.user}>: $text")
    }
}