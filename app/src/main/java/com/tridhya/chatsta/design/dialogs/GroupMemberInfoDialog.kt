package com.tridhya.chatsta.design.dialogs

import android.app.Dialog
import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import com.tridhya.chatsta.R
import com.tridhya.chatsta.databinding.DialogGroupMemberInfoBinding
import java.lang.reflect.Member

class GroupMemberInfoDialog(
    private val mContext: Context,
    private val member: Member,
    userName: String,
    private val isAdmin: Boolean,
    theme: Int,
) : Dialog(mContext, theme), View.OnClickListener {
    private lateinit var binding: DialogGroupMemberInfoBinding
    private var listener: ButtonListener? = null
    private var cancellable = true
    private var data = member
    private var name = userName

    companion object {
        fun getInstance(
            context: Context,
            member: Member,
            userName: String,
            isAdmin: Boolean,
            theme: Int = R.style.DefaultThemeDialog,
        ): GroupMemberInfoDialog {
            return GroupMemberInfoDialog(
                context,
                member,
                userName = userName,
                isAdmin,
                theme
            )
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setCanceledOnTouchOutside(cancellable)
        setCancelable(cancellable)

        binding = DialogGroupMemberInfoBinding.inflate(
            LayoutInflater.from(context),
            null,
            false
        )

        setContentView(binding.root)

        binding.tvUserName.text = name

        binding.tvViewProfile.setOnClickListener(this)
        /* binding.llRemoveFromGroup.isVisible =
             isAdmin && member.id != (mContext as ActivityBase).session?.user?.userId*/
        binding.tvRemoveFromGroup.setOnClickListener(this)
    }

    fun setListener(listener: ButtonListener?): GroupMemberInfoDialog {
        this.listener = listener
        return this
    }

    fun setCancellable(cancellable: Boolean): GroupMemberInfoDialog {
        this.cancellable = cancellable
        return this
    }

    interface ButtonListener {
        fun onViewProfileSelected(dialog: GroupMemberInfoDialog, memberId: String)
        fun onRemoveFromGroupSelected(dialog: GroupMemberInfoDialog, memberId: String)
    }

    override fun onClick(view: View?) {
        when (view?.id) {
            R.id.tvViewProfile -> {
                if (listener == null) dismiss()
//                else listener?.onViewProfileSelected(this, data.id.toString())
            }

            R.id.tvRemoveFromGroup -> {
                if (listener == null) dismiss()
//                else listener?.onRemoveFromGroupSelected(this, data.id.toString())
            }
        }
    }

}