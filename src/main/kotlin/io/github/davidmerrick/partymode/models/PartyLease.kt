package io.github.davidmerrick.partymode.models

import java.time.Instant
import java.time.temporal.ChronoUnit

data class PartyLease(
        val startTime: String,
        val timeout: Int) {
    fun isActive() = !Instant.now().isAfter(Instant.parse(startTime).plus(timeout.toLong(), ChronoUnit.HOURS))

    companion object {
        fun default() = PartyLease(Instant.now().minusMillis(1).toString(), 0)
    }
}
