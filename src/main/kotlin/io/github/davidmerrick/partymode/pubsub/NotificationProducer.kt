package io.github.davidmerrick.partymode.pubsub

import io.github.davidmerrick.partymode.config.SnsConfig
import mu.KotlinLogging
import software.amazon.awssdk.auth.credentials.AwsBasicCredentials
import software.amazon.awssdk.regions.Region
import software.amazon.awssdk.services.sns.SnsClient
import software.amazon.awssdk.services.sns.model.PublishRequest
import javax.inject.Singleton

private val log = KotlinLogging.logger {}

@Singleton
class NotificationProducer(private val config: SnsConfig) {
    private val client by lazy {
        val creds = AwsBasicCredentials.create(
            config.accessKeyId,
            config.secretAccessKey
        )
        SnsClient.builder()
            .region(Region.of(config.region))
            .credentialsProvider { creds }
            .build()
    }

    /**
     * Publishes message if notifier is enabled, otherwise does nothing
     */
    fun produce(message: String) {
        if (!config.enabled) {
            log.warn("Producer is disabled. Skipping notification.")
            return
        }

        log.info("Pushing notification to SNS")

        val request = PublishRequest.builder()
            .topicArn(config.topicArn)
            .message(message)
            .build()
        try {
            client.publish(request)
            log.info("Success: Pushed SNS notification")
        } catch (e: Exception) {
            log.error("Failed to notify SNS topic", e)
        }
    }
}
