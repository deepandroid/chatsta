package com.tridhya.chatsta.utils

import java.util.*

object NumberFormatter {
    private val suffixes: NavigableMap<Long, String> = TreeMap()

    init {
        suffixes[1000L] = "k"
        suffixes[1000000L] = "M"
        suffixes[1000000000L] = "B"
        suffixes[1000000000000L] = "T"
        suffixes[1000000000000000L] = "P"
        suffixes[1000000000000000000L] = "E"
    }

    fun formatInM(value: Long): String {
        return when {
            value > 1000000000000000000L -> {
                "${value / 1000000000000000000L}E"
            }
            value > 1000000000000000L -> {
                "${value / 1000000000000000L}P"
            }
            value > 1000000000000L -> {
                "${value / 1000000000000L}T"
            }
            value > 1000000000L -> {
                "${value / 1000000000L}B"
            }
            value > 1000000L -> {
                "${value / 1000000L}M"
            }
            value > 1000L -> {
                "${value / 1000L}k"
            }
            else -> {
                value.toString()
            }
        }
    }

    fun formatInM(value: Double): String {
        if (value == Double.MIN_VALUE) return format(Double.MIN_VALUE + 1)
        if (value < 0) return "-" + format(-value)
        if (value < 1000000) return getDecimalNumber(value) //deal with easy case
        val e: Map.Entry<Long, String> = suffixes.floorEntry(value.toLong())
        val divideBy = e.key
        val suffix = e.value
        val truncated = value / (divideBy / 10) //the number part of the output times 10
        val hasDecimal = truncated < 100 && truncated / 10.0 != (truncated / 10)
        return if (hasDecimal) getDecimalNumber((truncated / 10.0)) + suffix else getDecimalNumber((truncated / 10)) + suffix
    }


    fun format(value: String?): String {
        return getRoundNumber(value)
    }

    fun format(value: Double?): String {
        return getDecimalNumber(value)
    }

    fun getDecimalNumber(number: Double?): String {
        if (number == null || number.isNaN()) return "0.00"
        return String.format("%.2f", number)
    }

    fun getLongNumber(number: Double?): String {
        if (number == null || number.isNaN()) return "0"
        return number.toLong().toString()
    }

    fun getRoundNumber(number: Double?): String {
        if (number == null) return "0.00"
        return try {
            String.format("%.2f", number)
        } catch (e: Exception) {
            number.toString()
        }
    }

    fun getRoundNumber(number: String?): String {
        if (number.isNullOrBlank() || number.toDoubleOrNull() == null) return "0.00"
        return try {
            String.format("%.2f", number.toDouble())
        } catch (e: Exception) {
            number.toString()
        }
    }
}
