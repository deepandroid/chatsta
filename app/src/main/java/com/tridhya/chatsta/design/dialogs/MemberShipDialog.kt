package com.tridhya.chatsta.design.dialogs

import android.app.Dialog
import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import com.tridhya.chatsta.databinding.LayoutMembershipDialogBinding

class MemberShipDialog(context: Context) : Dialog(context) {

    private lateinit var binding: LayoutMembershipDialogBinding
    private var listener: ButtonListener? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setCanceledOnTouchOutside(false)
        setCancelable(false)
        window?.setBackgroundDrawableResource(android.R.color.transparent)

        binding = LayoutMembershipDialogBinding.inflate(
            LayoutInflater.from(context),
            null,
            false
        )

        setContentView(binding.root)
        binding.ivClose.setOnClickListener {
            if (listener == null) dismiss()
            else listener?.onNegativeBtnClicked(this)
        }

        binding.btnBuy.setOnClickListener {
            if (listener == null) dismiss()
            else listener?.onPositiveBtnClicked(this)
        }
    }

    interface ButtonListener {
        fun onPositiveBtnClicked(dialog: MemberShipDialog)

        fun onNegativeBtnClicked(dialog: MemberShipDialog) {
            dialog.dismiss()
        }
    }

    fun setListener(listener: ButtonListener?): MemberShipDialog {
        this.listener = listener
        return this
    }


}