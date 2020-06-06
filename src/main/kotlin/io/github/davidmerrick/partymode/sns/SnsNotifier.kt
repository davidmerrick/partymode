package io.github.davidmerrick.partymode.sns

import mu.KotlinLogging

private val log = KotlinLogging.logger {}

class SnsNotifier() {
//    fun notify(message: String){
//        log.info("Pushing notification to SNS")
//        val client = AmazonSNSAsyncClientBuilder.defaultClient()
//        val request = PublishRequest(config.sns.topicArn, message)
//        try {
//            val response = client.publish(request)
//            log.info("SNS messageId: ${response.messageId}")
//        } catch(e: Exception){
//            log.error("Failed to notify SNS topic", e)
//        }
//    }
}