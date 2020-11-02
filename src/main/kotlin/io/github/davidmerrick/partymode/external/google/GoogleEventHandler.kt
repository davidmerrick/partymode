package io.github.davidmerrick.partymode.external.google

import io.github.davidmerrick.partymode.storage.PartymodeStorage
import mu.KotlinLogging
import javax.inject.Singleton

private val log = KotlinLogging.logger {}

@Singleton
class GoogleEventHandler(
    private val storage: PartymodeStorage,
    private val mapper: GoogleObjectMapper
) {

    fun handle(event: GoogleEvent): String? {
        if (event.handler.name == "enable") {
            log.info("Enabling partymode")
            storage.enable()
            return constructGoogleResponse(event.session.id)
        }

        return null
    }

    private fun constructGoogleResponse(sessionId: String): String {
        val responseText = "Partymode enabled for 3 hours"
        val response = mapOf(
            "session" to mapOf(
                "id" to sessionId
            ),
            "prompt" to mapOf(
                "override" to false,
                "firstSimple" to mapOf(
                    "speech" to responseText,
                    "text" to responseText
                )
            ),
            "scene" to mapOf(
                "name" to "SceneName",
                "next" to mapOf(
                    "name" to "actions.scene.END_CONVERSATION"
                )
            )
        )

        return mapper.writeValueAsString(response)
    }
}
