package com.tridhya.chatsta.utils

import android.util.Log
import java.text.ParseException
import java.text.SimpleDateFormat
import java.util.*
import java.util.concurrent.TimeUnit

object DateTimeUtils {

    fun getFormattedDate(date: String): String {
        var spf = SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'", Locale.getDefault())
        val newDate: Date = spf.parse(date) as Date
        spf = SimpleDateFormat("dd.MM.yyyy")
        return spf.format(newDate)
    }

    fun covertTimeStampToText(dataDate: String?): String? {
        var convTime: String? = null
        val suffix = "Ago"
        try {
            val dateDiff = System.currentTimeMillis() - (dataDate?.toLong()!!)
            val second: Long = TimeUnit.MILLISECONDS.toSeconds(dateDiff)
            val minute: Long = TimeUnit.MILLISECONDS.toMinutes(dateDiff)
            val hour: Long = TimeUnit.MILLISECONDS.toHours(dateDiff)
            val day: Long = TimeUnit.MILLISECONDS.toDays(dateDiff)
            if (second < 60) {
                convTime = if (second < 2) {
                    "$second Second $suffix"
                } else {
                    "$second Seconds $suffix"
                }
            } else if (minute < 60) {
                convTime = if (minute < 2) {
                    "$minute Minute $suffix"
                } else {
                    "$minute Minutes $suffix"
                }
            } else if (hour < 24) {
                convTime = if (hour < 2) {
                    "$hour Hour $suffix"
                } else {
                    "$hour Hours $suffix"
                }
            } else if (day >= 7) {
                convTime = if (day > 360) {
                    (day / 360).toString() + " Years " + suffix
                } else if (day > 30) {
                    (day / 30).toString() + " Months " + suffix
                } else {
                    (day / 7).toString() + " Week " + suffix
                }
            } else if (day < 7) {
                convTime = if (day < 2) {
                    "$day Day $suffix"
                } else {
                    "$day Days $suffix"
                }
            }
        } catch (e: ParseException) {
            e.printStackTrace()
            Log.e("ConvTimeE", e.message.toString())
        }
        return convTime
    }

    fun covertTimeStampToTextNew(dataDate: String?): String? {
        var convTime: String? = null
        val suffix = "ago"
        try {
            val dateDiff = System.currentTimeMillis() - (dataDate?.toLong()!!)
            val second: Long = TimeUnit.MILLISECONDS.toSeconds(dateDiff)
            val minute: Long = TimeUnit.MILLISECONDS.toMinutes(dateDiff)
            val hour: Long = TimeUnit.MILLISECONDS.toHours(dateDiff)
            val day: Long = TimeUnit.MILLISECONDS.toDays(dateDiff)
            if (second < 60) {
                convTime = if (second < 30) {
                    "Now"
                } else {
                    "$second Seconds $suffix"
                }
            } else if (minute < 60) {
                convTime = "${minute}m $suffix"
            } else if (hour < 24) {
                convTime = "${hour}h $suffix"
            } else if (day >= 7) {
                convTime = if (day > 360) {
                    (day / 360).toString() + " Years " + suffix
                } else if (day > 30) {
                    (day / 30).toString() + " Months " + suffix
                } else {
                    (day / 7).toString() + " Week " + suffix
                }
            } else if (day < 7) {
                convTime = if (day == 1L) {
                    "Yesterday"
                } else {
                    "${day}Days $suffix"
                }
            }
        } catch (e: ParseException) {
            e.printStackTrace()
            Log.e("ConvTimeE", e.message.toString())
        }
        return convTime
    }

    fun stringToTime(timeString: String) {
        val dateFormat = SimpleDateFormat("mmss")
        val dateFormat2 = SimpleDateFormat("mm:ss")
        try {
            val date = dateFormat.parse(timeString)
            val out = dateFormat2.format(date)
            Log.e("Time", out)
        } catch (e: ParseException) {
        }
    }
}