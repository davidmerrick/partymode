package io.github.davidmerrick.partymode.controllers.call

import io.github.davidmerrick.partymode.external.twilio.TwilioHeaders.TWILIO_SIGNATURE
import io.github.davidmerrick.partymode.external.twilio.TwilioValidatorWrapper
import io.micronaut.context.annotation.Context
import io.micronaut.http.HttpRequest
import io.micronaut.http.HttpResponse
import io.micronaut.http.MediaType
import io.micronaut.http.annotation.Body
import io.micronaut.http.annotation.Controller
import io.micronaut.http.annotation.Header
import io.micronaut.http.annotation.Post
import io.micronaut.http.server.util.HttpHostResolver
import mu.KotlinLogging
import org.apache.http.client.utils.URIBuilder
import java.net.URI

private val log = KotlinLogging.logger {}

@Controller("/call")
class CallController(
        private val logic: CallHandlerLogic,
        private val validator: TwilioValidatorWrapper,
        private val resolver: HttpHostResolver
) {

    @Post(consumes = [MediaType.APPLICATION_FORM_URLENCODED],
            produces = [MediaType.APPLICATION_XML])
    fun handleCall(
            @Header(TWILIO_SIGNATURE) twilioSignature: String,
            @Context request: HttpRequest<String>,
            @Body body: String
    ): HttpResponse<String> {
        val requestUrl = resolveUri(request)
        if (!validator.validate(requestUrl, body, twilioSignature)) {
            return HttpResponse.badRequest("Failed to validate request")
        }

        val responseBody = logic.handleRequest(body)
        return HttpResponse.ok(responseBody)
    }

    private fun resolveUri(request: HttpRequest<String>): String {
        val resolvedUrl = resolver.resolve(request) + request.path

        // Cloud Run forwards https to http for the container. Swap out the scheme if this is the case
        if (request.headers.contains("x-forwarded-proto")) {
            return URIBuilder(URI.create(resolvedUrl))
                    .setScheme(request.headers["x-forwarded-proto"]!!)
                    .toString()
        }

        return resolvedUrl
    }
}