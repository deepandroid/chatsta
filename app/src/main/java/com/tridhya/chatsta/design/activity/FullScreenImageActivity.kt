package com.tridhya.chatsta.design.activity

import android.os.Bundle
import androidx.core.content.ContextCompat
import com.bumptech.glide.Glide
import com.tridhya.chatsta.R
import com.tridhya.chatsta.base.ActivityBase
import com.tridhya.chatsta.databinding.ActivityFullscreenImageBinding
import com.tridhya.chatsta.provider.Constants

class FullScreenImageActivity : ActivityBase() {
    private lateinit var binder: ActivityFullscreenImageBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binder = ActivityFullscreenImageBinding.inflate(layoutInflater)
        setContentView(binder.root)
        binder.lifecycleOwner = this
        window.statusBarColor = ContextCompat.getColor(this, R.color.black)

        Glide.with(this)
            .load(intent.getStringExtra(Constants.DATA))
            .placeholder(R.drawable.ic_placeholder_image)
            .error(R.drawable.ic_placeholder_image)
            .into(binder.image)

        binder.ivClose.setOnClickListener { finish() }
    }
}