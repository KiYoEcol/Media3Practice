package com.example.media3practice

import java.time.Duration
import java.time.Instant
import java.time.LocalDateTime
import java.time.ZoneId
import kotlin.math.round

fun numberFormat(number: Int): String {
    return if (number < 10000) {
        number.toString()
    } else if (number < 100000000) {
        val rounded = round(number.toDouble() / 10000).toInt().toString()
        "${rounded}万"
    } else {
        val rounded = round(number.toDouble() / 100000000).toInt().toString()
        "${rounded}億"
    }
}

fun numberFormat(number: Long): String {
    return if (number < 10000) {
        number.toString()
    } else if (number < 100000000) {
        val rounded = round(number.toDouble() / 10000).toLong().toString()
        "${rounded}万"
    } else {
        val rounded = round(number.toDouble() / 100000000).toLong().toString()
        "${rounded}億"
    }
}

fun timestampFormatAgo(timestamp:Long):String{
    val now = LocalDateTime.now()
    val dateTime = LocalDateTime.ofInstant(
        Instant.ofEpochSecond(timestamp),
        ZoneId.systemDefault()
    )
    val duration = Duration.between(dateTime, now)

    return when {
        duration.toHours() < 24 -> "${duration.toHours()}時間前"
        duration.toDays() < 30 -> "${duration.toDays()}日前"
        duration.toDays() < 365 -> "${duration.toDays() / 30}ヶ月前"
        else -> "${duration.toDays() / 365}年前"
    }
}