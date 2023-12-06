package com.tridhya.chatsta.utils

import android.content.Context
import com.tridhya.chatsta.Model.request.RegisterRequestModel
import com.google.gson.Gson
import com.tridhya.chatsta.Model.User
import com.tridhya.chatsta.R

class Session(val context: Context) {
    val pref =
        context.getSharedPreferences(context.getString(R.string.app_name), Context.MODE_PRIVATE)

    var isLoggedIn: Boolean
        get() = pref.contains(KEY_IS_LOGIN) && pref.getBoolean(KEY_IS_LOGIN, false)
        set(isLoggedIn) = storeDataByKey(KEY_IS_LOGIN, isLoggedIn)

    var savedLogin: RegisterRequestModel?
        get() {
            val gson = Gson()
            val json = getDataByKey(KEY_SAVED_USER_LOGIN, "")
            return gson.fromJson(json, RegisterRequestModel::class.java)
        }
        set(req) {
            val gson = Gson()
            val json = gson.toJson(req)
            pref.edit().putString(KEY_SAVED_USER_LOGIN, json).apply()
        }

    var user: User?
        get() {
            val gson = Gson()
            val json = getDataByKey(KEY_USER_INFO, "")
            return gson.fromJson(json, User::class.java)
        }
        set(user) {
            val gson = Gson()
            val json = gson.toJson(user)
            pref.edit().putString(KEY_USER_INFO, json).apply()
            isLoggedIn = true
        }

    var token: String
        get() = getDataByKey(TOKEN, "")
        set(token) = storeDataByKey(TOKEN, token)

    @JvmOverloads
    fun getDataByKey(Key: String, DefaultValue: String = ""): String {
        return if (pref.contains(Key)) {
            pref.getString(Key, DefaultValue).toString()
        } else {
            DefaultValue
        }
    }

    @JvmOverloads
    fun getDataByKey(Key: String, DefaultValue: Boolean = false): Boolean {
        return if (pref.contains(Key)) {
            pref.getBoolean(Key, DefaultValue)
        } else {
            DefaultValue
        }
    }

    fun storeDataByKey(key: String, Value: String) {
        pref.edit().putString(key, Value).apply()
    }

    fun storeDataByKey(key: String, Value: Boolean) {
        pref.edit().putBoolean(key, Value).apply()
    }

    operator fun contains(key: String): Boolean {
        return pref.contains(key)
    }

    fun remove(key: String) {
        pref.edit().remove(key).apply()
    }

    fun logout() {
        if (isLoggedIn) {
            /*val mDBReference =
                FirebaseDatabase.getInstance().reference.child(Constants.BUILD_CONFIG)

            val params: MutableMap<String, Any> = HashMap()
            params["isOnline"] = false
            params["lastSeen"] = System.currentTimeMillis()
            mDBReference
                .child(Constants.TABLE_USERS)
                .child(userId.toString())
                .updateChildren(params)*/
        }
        /*
                val notificationManager =
                    context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
                notificationManager.cancelAll()*/

        val saved = this.savedLogin

        pref.edit().clear().apply()

        this.savedLogin = saved
    }

    private companion object {
        private const val KEY_IS_LOGIN = "isLogin"
        private const val KEY_USER_INFO = "user"
        private const val TOKEN = "token"
        private const val KEY_SAVED_USER_LOGIN = "savedLogin"
    }
}