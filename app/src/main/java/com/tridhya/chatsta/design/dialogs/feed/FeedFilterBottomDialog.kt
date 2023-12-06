package com.tridhya.chatsta.design.dialogs.feed

import android.os.Build
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.annotation.RequiresApi
import com.tridhya.chatsta.R
import com.tridhya.chatsta.databinding.LayoutFeedFilterDialogBinding
import com.tridhya.chatsta.design.BaseBottomSheetDialogFragment
import com.tridhya.chatsta.provider.Constants

class FeedFilterBottomDialog :
    BaseBottomSheetDialogFragment(), View.OnClickListener {

    private lateinit var binding: LayoutFeedFilterDialogBinding

    companion object {
        private lateinit var listener: OptionListener
        var selectedValue: String? = null

        fun newInstance(
            selectedValue: String?,
            listener: OptionListener,
        ): FeedFilterBottomDialog {
            Companion.listener = listener
            Companion.selectedValue = selectedValue
            return FeedFilterBottomDialog()
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
        binding = LayoutFeedFilterDialogBinding.inflate(
            layoutInflater,
            container,
            false
        )
        return binding.root
    }

    @RequiresApi(Build.VERSION_CODES.Q)
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        if (selectedValue == Constants.MOST_RECENT) {
            binding.ivSelect1.visibility = View.VISIBLE
            binding.ivSelect2.visibility = View.INVISIBLE
        } else {
            binding.ivSelect2.visibility = View.VISIBLE
            binding.ivSelect1.visibility = View.INVISIBLE
        }
        binding.text1.setOnClickListener(this)
        binding.text2.setOnClickListener(this)
    }

    override fun onClick(view: View) {
        when (view.id) {
            R.id.text1 -> {
                binding.ivSelect1.visibility = View.VISIBLE
                binding.ivSelect2.visibility = View.INVISIBLE
                listener.onOptionSelected(this, Constants.MOST_RECENT)
            }

            R.id.text2 -> {
                binding.ivSelect2.visibility = View.VISIBLE
                binding.ivSelect1.visibility = View.INVISIBLE
                listener.onOptionSelected(this, Constants.MOST_POPULAR)
            }
        }
    }

    interface OptionListener {
        fun onOptionSelected(dialog: FeedFilterBottomDialog, filterBy: String)
    }
}