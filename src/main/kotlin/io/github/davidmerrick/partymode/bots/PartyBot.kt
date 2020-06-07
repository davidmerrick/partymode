package io.github.davidmerrick.partymode.bots

import io.github.davidmerrick.partymode.storage.PartymodeStorage
import io.github.davidmerrick.slakson.messages.SlackEvent
import mu.KotlinLogging
import javax.inject.Singleton

private val log = KotlinLogging.logger {}

val helpText = """
Usage: 
* `pm [1-5]`: Enable partymode for n hours
* `pm disable`: Disable partymode
""".trimIndent()

@Singleton
class PartyBot(private val storage: PartymodeStorage) {

    fun handle(event: SlackEvent): String {
        log.info("Handling message")
        operator fun Regex.contains(text: CharSequence): Boolean = this.matches(text)

        return when (val text = event.text.toLowerCase()) {
            in Regex(".*pm [1-5]$") -> {
                val regex = "[1-5]$".toRegex()
                val numHours = regex.find(text)!!.value.toInt()
                storage.enableForHours(numHours)
                val suffix = if (numHours > 1) "hours" else "hour"
                "partymode enabled for $numHours $suffix"
            }
            in Regex(".*pm disable.*") -> {
                storage.disablePartyMode()
                "partymode disabled"
            }
            in Regex(".*pm status.*") -> {
                val status = if (storage.isPartymodeEnabled()) {
                    "enabled"
                } else {
                    "disabled"
                }
                "partymode $status"
            }
            else -> helpText
        }
    }
}