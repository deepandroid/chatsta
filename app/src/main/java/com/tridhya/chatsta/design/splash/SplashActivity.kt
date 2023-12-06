package com.tridhya.chatsta.design.splash

import android.annotation.SuppressLint
import android.net.Uri
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import com.tridhya.chatsta.extensions.goToActivity
import com.tridhya.chatsta.R
import com.tridhya.chatsta.base.ActivityBase
import com.tridhya.chatsta.databinding.ActivitySplashBinding
import com.tridhya.chatsta.design.MainActivity

@SuppressLint("CustomSplashScreen")
class SplashActivity : ActivityBase() {
    private val binding: ActivitySplashBinding by binding(R.layout.activity_splash)
    private val handler = Handler(Looper.getMainLooper())
    var isPaused = false
    private val runnable = Runnable {
        startHome()
    }


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        installSplashScreen()
        showFullScreen()
        binding.videoView.setVideoURI(Uri.parse("android.resource://$packageName/raw/vid_splash"))
        binding.videoView.setMediaController(null)
        binding.videoView.setOnPreparedListener { binding.videoView.start() }
        binding.videoView.setOnCompletionListener { startHome() }
    }

    override fun onPause() {
        super.onPause()
        isPaused = true
    }

    override fun onResume() {
        super.onResume()
        if (isPaused) {
            handler.removeCallbacks(runnable)
            startHome()
        }
        isPaused = false
    }

    override fun onDestroy() {
        super.onDestroy()
        binding.unbind()
    }

    private fun startHome() {
        goToActivity<MainActivity>()
        finish()
    }
}