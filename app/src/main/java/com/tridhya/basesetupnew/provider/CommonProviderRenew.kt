package com.tridhya.basesetupnew.provider

import android.content.Context
import com.tridhya.basesetupnew.utils.Constant
import com.tridhya.basesetupnew.utils.Constant.APPLICATION_ID
import com.tridhya.basesetupnew.utils.Constant.DEVICE_ID
import com.tridhya.basesetupnew.utils.Constant.TIME_ZONE
import com.tridhya.basesetupnew.utils.Constant.logE
import com.tridhya.basesetupnew.utils.Constant.logI

import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ActivityRetainedComponent
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.android.scopes.ActivityRetainedScoped
import java.util.*
import javax.inject.Named

@Module
@InstallIn(ActivityRetainedComponent::class)
object CommonProviderRenew {

    @ActivityRetainedScoped
    @Provides
    @Named(TIME_ZONE)
    fun provideTimeZone(): String {
        return try {
            val tz: TimeZone = TimeZone.getDefault()
            logI(
                "TAG",
                "TIME ZONE " + "TimeZone   " + tz.getDisplayName(false, TimeZone.SHORT)
                    .toString() + " Timezon id :: " + tz.id
            )
            tz.id
        } catch (e: Exception) {
            e.localizedMessage?.let { logE("catch Error", it) }
            ""
        }
    }

    @ActivityRetainedScoped
    @Provides
    @Named(APPLICATION_ID)
    fun provideApplicationId(
        @Named(DEVICE_ID) deviceId: String
    ): String {
        return "${(System.currentTimeMillis() / 1000)}-$deviceId"
    }

    @ActivityRetainedScoped
    @Provides
    @Named(DEVICE_ID)
    fun provideDeviceId(@ApplicationContext applicationContext: Context): String {
        return Constant.getSubscriptionId(applicationContext)
    }
}