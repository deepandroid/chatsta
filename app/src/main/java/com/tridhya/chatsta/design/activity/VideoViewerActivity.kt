package com.tridhya.chatsta.design.activity

import android.annotation.SuppressLint
import android.content.pm.ActivityInfo
import android.net.Uri
import android.os.Bundle
import android.view.View
import android.view.Window
import android.view.WindowManager
import com.google.android.exoplayer2.ExoPlayer
import com.google.android.exoplayer2.MediaItem
import com.google.android.exoplayer2.Player
import com.tridhya.chatsta.base.ActivityBase
import com.tridhya.chatsta.databinding.ActivityVideoviewerBinding
import com.tridhya.chatsta.provider.Constants

class VideoViewerActivity : ActivityBase() {
    private var player: ExoPlayer? = null
    private lateinit var binding: ActivityVideoviewerBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityVideoviewerBinding.inflate(layoutInflater)
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        this.window.setFlags(
            WindowManager.LayoutParams.FLAG_FULLSCREEN,
            WindowManager.LayoutParams.FLAG_FULLSCREEN
        )
        setContentView(binding.root)
        hideSystemUi()

        binding.ivClose.setOnClickListener { finish() }

        player = ExoPlayer.Builder(this).build()
        binding.videoView.player = player
        binding.videoView.useController = true

        val videoUri = Uri.parse(intent.getStringExtra(Constants.DATA))

        try {
            val mediaItem = MediaItem.fromUri(videoUri)
            mediaItem.let { player?.setMediaItem(it) }
            player?.prepare()
            player?.play()
            playPlayer()
        } catch (e: Exception) {
            showToastLong("Failed to load video.")
            finish()
        }

        player?.addListener(object : Player.Listener {
            override fun onPlayerStateChanged(playWhenReady: Boolean, playbackState: Int) {
                if (playbackState == Player.STATE_ENDED) {
                    player!!.seekTo(0)
                    player!!.playWhenReady = false
                }
                if (playbackState == Player.STATE_READY) {
                    requestedOrientation =
                        if (binding.videoView.width < binding.videoView.height) ActivityInfo.SCREEN_ORIENTATION_PORTRAIT else ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE
                }
            }
        })
    }

    @SuppressLint("InlinedApi")
    private fun hideSystemUi() {
        binding.videoView.systemUiVisibility = (View.SYSTEM_UI_FLAG_LOW_PROFILE
                or View.SYSTEM_UI_FLAG_FULLSCREEN
                or View.SYSTEM_UI_FLAG_LAYOUT_STABLE
                or View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY
                or View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
                or View.SYSTEM_UI_FLAG_HIDE_NAVIGATION)
    }

    private fun playPlayer() {
        if (player != null) {
            player!!.playWhenReady = true
        }
    }

    private fun pausePlayer() {
        if (player != null) {
            player!!.playWhenReady = false
        }
    }

    private fun seekTo(positionInMS: Long) {
        if (player != null) {
            player!!.seekTo(positionInMS)
        }
    }

    private fun releasePlayer() {
        if (player != null) {
            player!!.release()
        }
    }

    override fun onResume() {
        super.onResume()
        playPlayer()
    }

    override fun onPause() {
        super.onPause()
        pausePlayer()
    }

    override fun onDestroy() {
        ActivityInfo.SCREEN_ORIENTATION_PORTRAIT
        releasePlayer()
        super.onDestroy()
    }

}