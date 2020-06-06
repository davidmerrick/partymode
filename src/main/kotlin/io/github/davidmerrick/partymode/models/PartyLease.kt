package io.github.davidmerrick.partymode.models

import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBHashKey
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBTable
import java.time.Instant
import java.time.temporal.ChronoUnit

@DynamoDBTable(tableName = "partymode")
data class PartyLease(
        @DynamoDBHashKey(attributeName = "start_time") val startTime: String,
        @DynamoDBHashKey(attributeName = "timeout") val timeout: Int
) {
    fun isActive() = !Instant.now().isAfter(Instant.parse(startTime).plus(timeout.toLong(), ChronoUnit.HOURS))

    companion object {
        fun default() = PartyLease(Instant.now().minusMillis(1).toString(), 0)
    }
}
