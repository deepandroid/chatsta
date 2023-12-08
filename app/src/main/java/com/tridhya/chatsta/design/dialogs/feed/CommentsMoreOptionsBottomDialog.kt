package com.tridhya.chatsta.design.dialogs.feed

import android.content.Context
import android.os.Build
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.annotation.RequiresApi
import com.tridhya.chatsta.R
import com.tridhya.chatsta.base.ActivityBase
import com.tridhya.chatsta.databinding.LayoutMoreOptionsDialogBinding
import com.tridhya.chatsta.design.BaseBottomSheetDialogFragment
import com.tridhya.chatsta.extensions.gone
import com.tridhya.chatsta.model.CommentsModel

class CommentsMoreOptionsBottomDialog :
    BaseBottomSheetDialogFragment(), View.OnClickListener {

    private lateinit var binding: LayoutMoreOptionsDialogBinding

    companion object {
        private lateinit var listener: OptionSelectedListener
        private lateinit var commentsModel: CommentsModel
        private var isReply = false

        fun newInstance(
            context: Context,
            commentsModel: CommentsModel,
            isReply: Boolean,
            listener: OptionSelectedListener,
        ): CommentsMoreOptionsBottomDialog {
            Companion.listener = listener
            Companion.commentsModel = commentsModel
            Companion.isReply = isReply
            return CommentsMoreOptionsBottomDialog()
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setStyle(STYLE_NORMAL, R.style.TransparentBottomSheetDialogTheme)
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View {
        binding = LayoutMoreOptionsDialogBinding.inflate(
            layoutInflater,
            container,
            false
        )
        return binding.root
    }

    @RequiresApi(Build.VERSION_CODES.Q)
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val currentUser = (context as ActivityBase).session?.user


        if (isReply) {
            binding.llEdit.visibility = View.GONE
            binding.llDelete.visibility = View.VISIBLE
            binding.llReport.visibility = View.GONE
        } else {
            if (commentsModel.commentedby?.userId == currentUser?.userId) {
                binding.llEdit.visibility = View.VISIBLE
                binding.llDelete.visibility = View.VISIBLE
                binding.llReport.visibility = View.GONE
            } else {
                binding.llEdit.gone()
                binding.llDelete.gone()
                binding.llReport.visibility = View.VISIBLE
            }
        }

        binding.llReport.setOnClickListener(this)
        binding.llEdit.setOnClickListener(this)
        binding.llDelete.setOnClickListener(this)
    }

    override fun onClick(view: View) {
        when (view.id) {
            R.id.llReport -> {
                dismiss()
                listener.onReportSelected(this, commentsModel)
            }

            R.id.llDelete -> {
                if (!isReply)
                    listener.onDeleteSelected(this, commentsModel)
                else
                    listener.onDeleteReplySelected(this, commentsModel)
                dismiss()
            }

            R.id.llEdit -> {
                dismiss()
                listener.onEditSelected(this, commentsModel)
            }
        }
    }

    interface OptionSelectedListener {
        fun onEditSelected(dialog: CommentsMoreOptionsBottomDialog, postModel: CommentsModel)
        fun onDeleteSelected(dialog: CommentsMoreOptionsBottomDialog, postModel: CommentsModel)
        fun onDeleteReplySelected(dialog: CommentsMoreOptionsBottomDialog, postModel: CommentsModel)
        fun onReportSelected(dialog: CommentsMoreOptionsBottomDialog, postModel: CommentsModel)
    }
}