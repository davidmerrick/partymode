package com.merricklabs.autobuzzer

import com.amazonaws.services.lambda.runtime.Context
import com.amazonaws.services.lambda.runtime.RequestHandler
import org.apache.logging.log4j.LogManager

class Handler:RequestHandler<Map<String, Any>, ApiGatewayResponse> {
    override fun handleRequest(input:Map<String, Any>, context:Context):ApiGatewayResponse {
        return ApiGatewayResponse.build {
            statusCode = 200
            objectBody = CallResponse("Go Serverless v1.x! Your Kotlin function executed successfully!", input)
            headers = mapOf("X-Powered-By" to "AWS Lambda & serverless")
        }
    }

    companion object {
        private val LOG = LogManager.getLogger(Handler::class.java)
    }
}
