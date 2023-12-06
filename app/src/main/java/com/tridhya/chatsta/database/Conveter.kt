package com.tridhya.chatsta.database

import androidx.room.TypeConverter

class Conveter {

    @TypeConverter
    fun fromAny(value: Any?): String {
        return value.toString()
    }



}