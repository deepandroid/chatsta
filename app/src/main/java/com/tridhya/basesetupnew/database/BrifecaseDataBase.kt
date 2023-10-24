package com.tridhya.basesetupnew.database

import androidx.room.Database
import androidx.room.RoomDatabase
import com.tridhya.basesetupnew.dao.UserDataDao


import com.tridhya.basesetupnew.response.LoginResponse
import com.tridhya.basesetupnew.utils.Constant.DatabaseVersion

@Database(
    entities = [LoginResponse::class],
    version = DatabaseVersion,
    exportSchema = false
)
abstract class BrifecaseDataBase : RoomDatabase() {
    abstract fun userDataDao(): UserDataDao

}