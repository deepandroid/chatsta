package com.tridhya.basesetupnew.design.splash

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import com.tridhya.basesetupnew.R
import com.tridhya.basesetupnew.base.ActivityBase
import com.tridhya.basesetupnew.databinding.ActivitySplashBinding
import com.tridhya.basesetupnew.design.signin.SigninActivity
import kotlinx.coroutines.runBlocking

@SuppressLint("CustomSplashScreen")
class SplashActivity : ActivityBase() {
    private val binding: ActivitySplashBinding by binding(R.layout.activity_splash)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        installSplashScreen()
        binding.lifecycleOwner = this
        Handler(Looper.getMainLooper()).postDelayed({
            loginLogic()
        }, 2000)
    }

    private fun loginLogic() {
        runBlocking {
            val i = Intent(this@SplashActivity, SigninActivity::class.java)
            startActivity(i)
            finish()
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        binding.unbind()
    }
}