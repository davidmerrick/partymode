package io.github.davidmerrick.partymode.controllers

import io.github.davidmerrick.partymode.external.slack.SlackMessageHandler
import io.github.davidmerrick.slakson.messages.SlackMessage
import io.micronaut.http.MediaType
import io.micronaut.http.annotation.Body
import io.micronaut.http.annotation.Controller
import io.micronaut.http.annotation.Post
import javax.annotation.security.PermitAll

@Controller("/slack")
class SlackController(
    private val handler: SlackMessageHandler
) {

    @Post("/events",
        consumes = [MediaType.APPLICATION_JSON],
        produces = [MediaType.TEXT_PLAIN]
    )
    @PermitAll
    fun handleEvent(@Body message: SlackMessage): String {
        return handler.handle(message) ?: ""
    }
}
