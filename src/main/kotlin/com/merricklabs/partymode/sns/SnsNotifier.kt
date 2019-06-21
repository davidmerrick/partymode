package com.merricklabs.partymode.sns

import com.amazonaws.services.sns.AmazonSNSAsyncClientBuilder
import com.amazonaws.services.sns.model.PublishRequest
import com.merricklabs.partymode.config.PartymodeConfig
import mu.KotlinLogging
import org.koin.core.KoinComponent
import org.koin.core.inject

private val log = KotlinLogging.logger {}

class SnsNotifier : KoinComponent {
    private val config: PartymodeConfig by inject()

    fun notify(message: String){
        log.info("Pushing notification to SNS")
        val client = AmazonSNSAsyncClientBuilder.defaultClient()
        val request = PublishRequest(config.sns.topicArn, message)
        try {
            val response = client.publish(request)
            log.info("SNS messageId: ${response.messageId}")
        } catch(e: Exception){
            log.error("Failed to notify SNS topic", e)
        }
    }
}