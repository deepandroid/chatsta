package com.tridhya.chatsta.design.dialogs

import android.app.Dialog
import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import com.tridhya.chatsta.R
import com.tridhya.chatsta.databinding.DialogSelectDestructChatTimeBinding

class SelectDestructChatTimeDialog(
    context: Context,
    selectedDuration: String,
    theme: Int,
) : Dialog(context, theme), View.OnClickListener {

    private lateinit var binding: DialogSelectDestructChatTimeBinding
    private var listener: ButtonListener? = null
    private var selectedValue = selectedDuration
    private var cancellable = true

    companion object {
        fun getInstance(
            context: Context,
            selectedDuration: String,
            theme: Int = R.style.DefaultThemeDialog,
        ): SelectDestructChatTimeDialog {
            return SelectDestructChatTimeDialog(
                context,
                selectedDuration,
                theme
            )
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setCanceledOnTouchOutside(cancellable)
        setCancelable(cancellable)

        binding = DialogSelectDestructChatTimeBinding.inflate(
            LayoutInflater.from(context),
            null,
            false
        )

        setContentView(binding.root)

        when (selectedValue) {
            context.getString(R.string._5min) -> {
                binding.tv1.isSelected = true
            }

            context.getString(R.string._1h) -> {
                binding.tv2.isSelected = true
            }

            context.getString(R.string._1w) -> {
                binding.tv3.isSelected = true
            }

            context.getString(R.string.destruct) -> {
                binding.tv4.isSelected = true
            }
        }

        binding.tv1.setOnClickListener(this)
        binding.tv2.setOnClickListener(this)
        binding.tv3.setOnClickListener(this)
        binding.tv4.setOnClickListener(this)
    }

    fun setListener(listener: ButtonListener?): SelectDestructChatTimeDialog {
        this.listener = listener
        return this
    }

    fun setCancellable(cancellable: Boolean): SelectDestructChatTimeDialog {
        this.cancellable = cancellable
        return this
    }

    interface ButtonListener {
        fun onDurationSelected(dialog: SelectDestructChatTimeDialog, duration: String)
    }

    override fun onClick(view: View?) {
        when (view?.id) {
            R.id.tv1 -> {
                if (listener == null) dismiss()
                else listener?.onDurationSelected(this, binding.tv1.text.toString())
            }

            R.id.tv2 -> {
                if (listener == null) dismiss()
                else listener?.onDurationSelected(this, binding.tv2.text.toString())
            }

            R.id.tv3 -> {
                if (listener == null) dismiss()
                else listener?.onDurationSelected(this, binding.tv3.text.toString())
            }

            R.id.tv4 -> {
                if (listener == null) dismiss()
                else listener?.onDurationSelected(this, binding.tv4.text.toString())
            }
        }
    }
}
