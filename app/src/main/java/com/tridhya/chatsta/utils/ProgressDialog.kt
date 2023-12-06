package com.tridhya.chatsta.utils

import android.app.AlertDialog
import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import com.bumptech.glide.Glide
import com.tridhya.chatsta.R
import com.tridhya.chatsta.databinding.LayoutProgressLoadingBinding

class ProgressDialog constructor(
    context: Context,
) :
    AlertDialog(context, R.style.ProgressDialog) {

    private lateinit var binding: LayoutProgressLoadingBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = LayoutProgressLoadingBinding.inflate(
            LayoutInflater.from(context),
            null,
            false
        )
        setContentView(binding.root)
        setCanceledOnTouchOutside(false)
        setCancelable(false)
        Glide.with(context)
            .load(R.raw.custom_app_loader)
            .centerCrop()
            .into(binding.customLoadingImageView)
    }
}