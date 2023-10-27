package com.tridhya.basesetupnew.provider


import androidx.room.RoomDatabase
import com.tridhya.basesetupnew.database.UserDatabase


import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
class DataBaseProvider {

    @Provides
    @Singleton
    fun providesUserDatabase(
        userDatabaseBuilder: RoomDatabase.Builder<UserDatabase>
    ): UserDatabase = userDatabaseBuilder.build()



}