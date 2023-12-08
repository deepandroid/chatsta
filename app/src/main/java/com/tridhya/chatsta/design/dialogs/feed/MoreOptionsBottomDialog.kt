package com.tridhya.chatsta.design.dialogs.feed

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
import com.tridhya.chatsta.model.PostModel

class MoreOptionsBottomDialog :
    BaseBottomSheetDialogFragment(), View.OnClickListener {

    private lateinit var binding: LayoutMoreOptionsDialogBinding

    companion object {
        private lateinit var listener: OptionSelectedListener
        private lateinit var postModel: PostModel

        fun newInstance(
            postModel: PostModel,
            listener: OptionSelectedListener,
        ): MoreOptionsBottomDialog {
            Companion.listener = listener
            Companion.postModel = postModel
            return MoreOptionsBottomDialog()
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
        if (postModel.postedBy?.userId == currentUser?.userId) {
            binding.llEdit.visibility = View.VISIBLE
            binding.llDelete.visibility = View.VISIBLE
            binding.llReport.visibility = View.GONE
        } else {
            binding.llEdit.visibility = View.GONE
            binding.llDelete.visibility = View.GONE
            binding.llReport.visibility = View.VISIBLE
        }
        binding.llReport.setOnClickListener(this)
        binding.llEdit.setOnClickListener(this)
        binding.llDelete.setOnClickListener(this)
    }

    override fun onClick(view: View) {
        when (view.id) {
            R.id.llReport -> {
                dismiss()
                listener.onReportSelected(this, postModel)
            }

            R.id.llDelete -> {
                listener.onDeleteSelected(this, postModel)
                dismiss()
            }

            R.id.llEdit -> {
                dismiss()
                listener.onEditSelected(this, postModel)
            }
        }
    }

    interface OptionSelectedListener {
        fun onEditSelected(dialog: MoreOptionsBottomDialog, postModel: PostModel)
        fun onDeleteSelected(dialog: MoreOptionsBottomDialog, postModel: PostModel)
        fun onReportSelected(dialog: MoreOptionsBottomDialog, postModel: PostModel)
    }
}