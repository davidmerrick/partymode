package com.merricklabs.partymode.util

import java.time.Instant
import java.time.temporal.ChronoUnit

object PartymodeUtil {
    fun shouldBuzz(startTime: Instant, timeoutHours: Int) = !Instant.now().isAfter(startTime.plus(timeoutHours.toLong(), ChronoUnit.HOURS))
}