package com.example.media3practice

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