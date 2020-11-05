package io.github.davidmerrick.partymode.controllers

import com.fasterxml.jackson.module.kotlin.readValue
import io.github.davidmerrick.partymode.external.google.GoogleEvent
import io.github.davidmerrick.partymode.external.google.GoogleEventHandler
import io.github.davidmerrick.partymode.external.google.GoogleObjectMapper
import io.micronaut.http.HttpHeaders
import io.micronaut.http.HttpResponse
import io.micronaut.http.MediaType
import io.micronaut.http.annotation.Body
import io.micronaut.http.annotation.Controller
import io.micronaut.http.annotation.Header
import io.micronaut.http.annotation.Post

@Controller("/google")
class GoogleAssistantController(
    private val handler: GoogleEventHandler,
    private val mapper: GoogleObjectMapper
) {
    @Post("/events",
        consumes = [MediaType.APPLICATION_JSON],
        produces = [MediaType.APPLICATION_JSON]
    )
    fun handleEvent(
        @Header(HttpHeaders.AUTHORIZATION) authHeader: String?,
        @Body eventString: String
    ): HttpResponse<String> {
        // Todo: Add validation here
        authHeader ?: return HttpResponse.unauthorized()

        val event = mapper.readValue<GoogleEvent>(eventString)
        return HttpResponse.ok(handler.handle(event) ?: "")
    }
}
