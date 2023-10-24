package com.tridhya.basesetupnew.utils

import android.app.Dialog
import android.content.Context
import android.os.Bundle
import android.view.Window
import android.widget.TextView
import com.tridhya.basesetupnew.R

class CountdownDialog(context: Context) : Dialog(context) {
    private lateinit var countdownTextView: TextView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        requestWindowFeature(Window.FEATURE_NO_TITLE)
        setContentView(R.layout.dialog_countdown)

        countdownTextView = findViewById(R.id.countdownTextView)
    }

    // Update the countdown value
    fun updateCountdown(value: Int) {
        countdownTextView.text = "App will Restart in $value sec"
    }
}