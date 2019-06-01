package com.merricklabs.partymode

import org.koin.standalone.KoinComponent

class PartymodeConfig : KoinComponent {

    val dynamoDb = DynamoDb()
    val sns = Sns()
    val phone = Phone()
    val slack = Slack()

    inner class Sns {
        val topicArn: String? = System.getenv("SNS_TOPIC")
    }

    inner class DynamoDb {
        val endpoint = System.getenv("DYNAMODB_ENDPOINT") ?: "https://dynamodb.us-west-2.amazonaws.com"
        val region = System.getenv("DYNAMODB_REGION") ?: "us-west-2"
        val tableName= System.getenv("DYNAMODB_TABLE_NAME") ?: "partymode"
    }

    inner class Phone {
        val myNumber: String = System.getenv("MY_NUMBER")
    }

    inner class Slack {
        val webhookUri: String = System.getenv("SLACK_WEBHOOK_URI")
        val botToken: String = System.getenv("BOT_TOKEN")
        val botName = System.getenv("BOT_NAME") ?: "partybot"
    }
}