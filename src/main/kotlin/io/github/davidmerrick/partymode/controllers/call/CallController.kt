package io.github.davidmerrick.partymode.controllers.call

import io.github.davidmerrick.partymode.external.twilio.TwilioHeaders.TWILIO_SIGNATURE
import io.github.davidmerrick.partymode.external.twilio.TwilioValidatorWrapper
import io.micronaut.context.annotation.Context
import io.micronaut.http.HttpResponse
import io.micronaut.http.MediaType
import io.micronaut.http.annotation.Body
import io.micronaut.http.annotation.Controller
import io.micronaut.http.annotation.Header
import io.micronaut.http.annotation.Post
import java.net.URI

@Controller("/call")
class CallController(
        private val logic: CallHandlerLogic,
        private val validator: TwilioValidatorWrapper
) {

    @Post(produces = [MediaType.APPLICATION_XML])
    fun handleCall(
            @Header(TWILIO_SIGNATURE) twilioSignature: String,
            @Context requestUrl: URI,
            @Body body: String
    ): HttpResponse<String> {
        if (!validator.validate(requestUrl.toString(), body, twilioSignature)) {
            return HttpResponse.badRequest("Failed to validate request")
        }

        val responseBody = logic.handleRequest(body)
        return HttpResponse.ok(responseBody)
    }
}