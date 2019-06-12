package com.merricklabs.partymode.config

class SnsConfig {
    val topicArn: String? = System.getenv("SNS_TOPIC")
}