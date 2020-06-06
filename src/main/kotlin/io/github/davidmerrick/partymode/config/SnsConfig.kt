package io.github.davidmerrick.partymode.config

class SnsConfig {
    val topicArn: String? = System.getenv("SNS_TOPIC")
}