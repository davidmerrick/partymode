package com.merricklabs.partymode.util

import java.time.Instant
import java.time.temporal.ChronoUnit

object PartymodeUtil {
    fun shouldBuzz(date: Instant, timeoutHours: Int) = !Instant.now().isAfter(date.plus(timeoutHours.toLong(), ChronoUnit.HOURS))
}