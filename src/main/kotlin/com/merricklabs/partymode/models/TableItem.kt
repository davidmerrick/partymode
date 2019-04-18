package com.merricklabs.partymode.models

import java.time.Instant

data class TableItem(val date: Instant, val timeoutHours: Int) {
    constructor(date: String, timeoutHours: Int) : this(Instant.parse(date), timeoutHours)
}