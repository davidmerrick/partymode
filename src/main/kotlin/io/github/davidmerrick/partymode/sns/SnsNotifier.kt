package io.github.davidmerrick.partymode.sns

import com.amazonaws.services.sns.AmazonSNSAsyncClientBuilder
import com.amazonaws.services.sns.model.PublishRequest
import io.github.davidmerrick.partymode.config.PartymodeConfig
import mu.KotlinLogging
import javax.inject.Singleton

private val log = KotlinLogging.logger {}

@Singleton
class SnsNotifier(private val config: PartymodeConfig) {
    fun notify(message: String) {
        log.info("Pushing notification to SNS")
        val client = AmazonSNSAsyncClientBuilder.defaultClient()
        val request = PublishRequest(config.sns.topicArn, message)
        try {
            val response = client.publish(request)
            log.info("SNS messageId: ${response.messageId}")
        } catch (e: Exception) {
            log.error("Failed to notify SNS topic", e)
        }
    }
}