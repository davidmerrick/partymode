package com.merricklabs.partymode.storage

import com.amazonaws.client.builder.AwsClientBuilder
import com.amazonaws.services.dynamodbv2.AmazonDynamoDB
import com.amazonaws.services.dynamodbv2.AmazonDynamoDBClientBuilder
import com.amazonaws.services.dynamodbv2.document.DynamoDB
import com.amazonaws.services.dynamodbv2.document.Table
import com.amazonaws.services.dynamodbv2.model.AttributeValue
import com.amazonaws.services.dynamodbv2.model.ScanRequest
import com.merricklabs.partymode.config.DynamoDbConfig
import com.merricklabs.partymode.config.PartymodeConfig
import com.merricklabs.partymode.models.PartyLease
import mu.KotlinLogging
import org.koin.core.KoinComponent
import org.koin.core.inject
import java.time.Instant

private val log = KotlinLogging.logger {}

class PartymodeStorage : KoinComponent {

    private val config by inject<PartymodeConfig>()
    private val dynamoDbConfig: DynamoDbConfig
    private val table: Table
    private val client: AmazonDynamoDB

    init {
        this.dynamoDbConfig = config.dynamoDb

        val dynamoDbConfig = config.dynamoDb
        client = AmazonDynamoDBClientBuilder.standard()
                .withEndpointConfiguration(
                        AwsClientBuilder.EndpointConfiguration(dynamoDbConfig.endpoint, dynamoDbConfig.region)
                )
                .build()

        val dynamoDB = DynamoDB(client)
        this.table = dynamoDB.getTable(dynamoDbConfig.tableName)
    }

    fun disablePartyMode() = enableForHours(0)

    fun enableForHours(numHours: Int) {
        log.info("Saving $numHours to db")
        client.putItem(dynamoDbConfig.tableName,
                mapOf("start_time" to AttributeValue(Instant.now().toString()),
                        "timeout" to AttributeValue(numHours.toString()))
        )
    }

    fun isPartymodeEnabled() = getLatestItem().isActive()

    private fun getLatestItem(): PartyLease {
        val scanRequest = ScanRequest()
                .withTableName(dynamoDbConfig.tableName)
        val items = client.scan(scanRequest).items
        val item = items.maxBy { Instant.parse(it["start_time"]!!.s) }

        item?.let {
            log.info("Got item: $item")
            return PartyLease(item["start_time"]!!.s, item["timeout"]!!.s.toInt())
        }

        log.info("No item found. Returning default.")
        return PartyLease.default()
    }
}