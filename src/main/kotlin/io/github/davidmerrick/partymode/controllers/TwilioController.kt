package io.github.davidmerrick.partymode.controllers

import io.github.davidmerrick.partymode.controllers.logic.CallHandlerLogic
import io.micronaut.http.MediaType
import io.micronaut.http.annotation.Body
import io.micronaut.http.annotation.Controller
import io.micronaut.http.annotation.Post

@Controller("/call")
class TwilioController(private val logic: CallHandlerLogic) {

    @Post(consumes = [MediaType.APPLICATION_FORM_URLENCODED],
            produces = [MediaType.APPLICATION_XML])
    fun handleCall(@Body body: String) {
        logic.handleRequest(body)
    }
}