package com.example.common

import kotlinx.datetime.Instant
import kotlinx.datetime.LocalDateTime
import kotlinx.datetime.TimeZone
import kotlinx.datetime.toLocalDateTime


fun Long.toLocalDateTime(): LocalDateTime {
    return Instant.fromEpochMilliseconds(this).toLocalDateTime(TimeZone.currentSystemDefault())
}

fun Long.toShortDate(): String {
    return this.toLocalDateTime().toShortDate()
}

fun Long.toLongDate(): String {
    return this.toLocalDateTime().toLongDate()
}
