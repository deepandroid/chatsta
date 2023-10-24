package com.tridhya.basesetupnew.dao

import androidx.room.*
import com.tridhya.basesetupnew.response.LoginResponse
import com.tridhya.basesetupnew.utils.Constant.TABLE_LOGIN

@Dao
@Entity(tableName = TABLE_LOGIN)
interface UserDataDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertUserData(data: List<LoginResponse>)

    @Query("SELECT * FROM $TABLE_LOGIN")
    fun getUserData(): List<LoginResponse>

    @Query("SELECT * FROM $TABLE_LOGIN LIMIT 1")
    fun getSingleUserData(): LoginResponse?

    @Query("DELETE FROM $TABLE_LOGIN")
    suspend fun deleteUserData()
}