package com.example.common

import kotlinx.datetime.LocalDateTime
import kotlinx.datetime.format
import kotlinx.datetime.format.FormatStringsInDatetimeFormats
import kotlinx.datetime.format.byUnicodePattern

@OptIn(FormatStringsInDatetimeFormats::class)
fun LocalDateTime.toShortDate(): String {
    val formatter = LocalDateTime.Format {
        byUnicodePattern("dd-MM-yyyy")
    }
    return this.format(formatter)
}

@OptIn(FormatStringsInDatetimeFormats::class)
fun LocalDateTime.toLongDate(): String {
    val formatter = LocalDateTime.Format {
        byUnicodePattern("yyyy年MM月dd日 HH:mm")
    }
    return this.format(formatter)
}