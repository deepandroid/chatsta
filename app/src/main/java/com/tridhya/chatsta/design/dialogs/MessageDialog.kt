package com.tridhya.chatsta.design.dialogs

import android.app.Dialog
import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import com.tridhya.chatsta.extensions.gone
import com.tridhya.chatsta.extensions.visible
import com.tridhya.chatsta.R
import com.tridhya.chatsta.databinding.DialogMessageBinding

class MessageDialog(
    context: Context,
    private var titleText: String?,
    private var message: String?,
    theme: Int,
) : Dialog(context, theme) {

    private lateinit var binding: DialogMessageBinding
    private var listener: ButtonListener? = null
    private var title: String? = titleText
    private var positiveTxt = context.getString(R.string.continue_txt)
    private var negativeTxt = context.getString(R.string.cancel)
    private var negativeBtnVisibility = View.GONE
    private var cancellable = false
    private var icon = R.drawable.ic_delete_rounded

    companion object {
        fun getInstance(
            context: Context,
            message: String,
            theme: Int = R.style.DefaultThemeDialog,
        ): MessageDialog {
            return MessageDialog(
                context,
                titleText = context.getString(R.string.please_note),
                message,
                theme
            )
        }

        fun getInstance(
            context: Context,
            titleText: String,
            message: String,
            theme: Int = R.style.DefaultThemeDialog,
        ): MessageDialog {
            return MessageDialog(context, titleText = titleText, message, theme)
        }

        fun getInstance(
            context: Context,
            theme: Int = R.style.DefaultThemeDialog,
        ): MessageDialog {
            return MessageDialog(
                context,
                titleText = context.getString(R.string.please_note),
                null,
                theme
            )
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setCanceledOnTouchOutside(cancellable)
        setCancelable(cancellable)

        binding = DialogMessageBinding.inflate(
            LayoutInflater.from(context),
            null,
            false
        )

        setContentView(binding.root)
        if (!title.isNullOrBlank()) {
            binding.tvTitle.visible()
            binding.tvTitle.text = title
        } else {
            binding.tvTitle.gone()
        }
        if (!message.isNullOrBlank()) binding.tvMessage.text = message
        if (!positiveTxt.isNullOrBlank()) binding.btnOk.text = positiveTxt
        if (!negativeTxt.isNullOrBlank()) binding.btnCancel.text = negativeTxt
        binding.btnCancel.visibility = negativeBtnVisibility
        binding.ivIcon.setImageResource(icon)

        binding.btnOk.setOnClickListener {
            if (listener == null) dismiss()
            else listener?.onPositiveBtnClicked(this)
        }

        binding.btnCancel.setOnClickListener {
            if (listener == null) dismiss()
            else listener?.onNegativeBtnClicked(this)
        }
    }

    fun setListener(listener: ButtonListener?): MessageDialog {
        this.listener = listener
        return this
    }

    fun setNegativeButton(text: String): MessageDialog {
        this.negativeBtnVisibility = View.VISIBLE
        this.negativeTxt = text
        return this
    }

    fun setNegativeButton(text: Int): MessageDialog {
        this.negativeBtnVisibility = View.VISIBLE
        this.negativeTxt = context.getString(text)
        return this
    }

    fun setPositiveButtonText(text: String): MessageDialog {
        this.positiveTxt = text
        return this
    }

    fun setPositiveButtonText(text: Int): MessageDialog {
        this.positiveTxt = context.getString(text)
        return this
    }

    fun setMessage(text: Int): MessageDialog {
        this.message = context.getString(text)
        return this
    }

    fun setMessage(text: String): MessageDialog {
        this.message = text
        return this
    }

    fun setCancellable(cancellable: Boolean): MessageDialog {
        this.cancellable = cancellable
        return this
    }

    fun setTitle(title: String?): MessageDialog {
        this.title = title
        return this
    }

    fun setIcon(icon: Int): MessageDialog {
        this.icon = icon
        return this
    }

    interface ButtonListener {
        fun onPositiveBtnClicked(dialog: MessageDialog)

        fun onNegativeBtnClicked(dialog: MessageDialog) {
            dialog.dismiss()
        }
    }
}
