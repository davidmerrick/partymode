package io.github.davidmerrick.partymode.controllers

import io.github.davidmerrick.partymode.controllers.logic.CallHandlerLogic
import io.github.davidmerrick.partymode.external.twilio.TwilioHeaders.TWILIO_SIGNATURE
import io.micronaut.http.MediaType
import io.micronaut.http.annotation.Body
import io.micronaut.http.annotation.Controller
import io.micronaut.http.annotation.Post
import jdk.nashorn.internal.runtime.regexp.joni.Config.log
import mu.KotlinLogging

private val log = KotlinLogging.logger {}

@Controller("/call")
class TwilioController(private val logic: CallHandlerLogic) {

    @Post(produces = [MediaType.APPLICATION_XML])
    fun handleCall(
            @HeaderParam(TWILIO_SIGNATURE) twilioSignature: String?,
            @Body body: String
    ) {
        validateRequest(twilioSignature, body)
        logic.handleRequest(body)
    }

    private fun validateRequest(twilioSignature: String?, body: String): Boolean {
        if (twilioSignature == null) {
            log.warn("Request headers does not contain Twilio signature.")
            return false
        }

        val twilioParams = TwilioParams(request.body)
        val requestUrl = request.getRequestUrl()
        if (!twilioHelpers.validateRequest(twilioParams, requestUrl, headers[TWILIO_SIGNATURE]!!)) {
            log.warn("Request did not match signature. " +
                    "Request url: $requestUrl, Signature: ${headers[TWILIO_SIGNATURE]}")
            return false
        }

        return true
    }
}