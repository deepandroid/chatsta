package com.tridhya.chatsta.design.dialogs.account

import android.app.Dialog
import android.os.Bundle
import android.util.Patterns
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import com.tridhya.chatsta.model.User
import com.tridhya.chatsta.R
import com.tridhya.chatsta.base.ActivityBase
import com.tridhya.chatsta.databinding.LayoutRequestForContentProviderBinding
import com.tridhya.chatsta.design.BaseBottomSheetDialogFragment
import com.tridhya.chatsta.design.viewModel.ProfileViewModel
import com.tridhya.chatsta.extensions.isEmail

class RequestForContentProviderDialog :
    BaseBottomSheetDialogFragment(), View.OnClickListener {
    private lateinit var binding: LayoutRequestForContentProviderBinding
    private val viewModel by lazy { ProfileViewModel(requireContext()) }
    private lateinit var user: User

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setStyle(STYLE_NORMAL, R.style.TransparentBottomSheetDialogTheme)
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View {
        binding = LayoutRequestForContentProviderBinding.inflate(
            layoutInflater,
            container,
            false
        )
        binding.viewModel = viewModel
        binding.lifecycleOwner = viewLifecycleOwner
        initViews()
        setObservers()
        return binding.root
    }

    private fun setObservers() {
        /*viewModel.responseContentProviderRequest.observe(viewLifecycleOwner) {
            viewModel.isLoading.value = false
            showCloseDialog("Congratulations you have sent a request to be Paid Content Provider.\nWe will review your request and reply as soon as possible.\nThank you!")
        }*/
    }

    private fun initViews() {
        user = (context as ActivityBase).session?.user!!
        if (!user.email.isNullOrBlank()
            && !user.email.isNullOrEmpty()
            && !user.email.equals("null", true)
        )
            viewModel.email.set(user.email)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val sheetContainer = requireView().parent as? ViewGroup ?: return
        sheetContainer.layoutParams.height = ViewGroup.LayoutParams.MATCH_PARENT
        binding.ivClose.setOnClickListener(this)
        binding.btnCancel.setOnClickListener(this)
        binding.btnSendRequest.setOnClickListener(this)
    }

    override fun onClick(view: View?) {
        when (view?.id) {
            R.id.ivClose, R.id.btnCancel -> {
                (context as ActivityBase).preventDoubleClick(view)
                dialog?.dismiss()
            }

            R.id.btnSendRequest -> {
                (context as ActivityBase).preventDoubleClick(view)
                validate()
            }
        }
    }

    private fun showCloseDialog(message: String) {
        val closeDialog = Dialog(requireContext(), R.style.DefaultThemeDialog)
        closeDialog.setContentView(R.layout.dialog_close)
        closeDialog.show()
        val title = closeDialog.findViewById<TextView>(R.id.tvTitle)
        val msg = closeDialog.findViewById<TextView>(R.id.tvMessage)
        val btnCancel = closeDialog.findViewById<TextView>(R.id.btnCancel)
        btnCancel.text = getString(R.string.close)
        title.text = getString(R.string.success)
        msg.text = message
        btnCancel.setOnClickListener {
            closeDialog.dismiss()
            dialog?.dismiss()
        }
    }

    fun validate() {
        when {
            viewModel.email.get().isNullOrBlank() -> {
                showToastShort(getString(R.string.err_enter_email))
            }

            !viewModel.email.get().toString().trim().isEmail() -> {
                showToastShort(getString(R.string.err_enter_valid_email))
            }

            viewModel.contentCreatedLink.get().isNullOrBlank() -> {
                showToastShort(getString(R.string.err_existing_content_created))
            }

            !Patterns.WEB_URL.matcher(viewModel.contentCreatedLink.get().toString().trim())
                .matches() -> {
                showToastShort(getString(R.string.err_url))
            }

            !viewModel.tikTokLink.get().isNullOrBlank() && !Patterns.WEB_URL.matcher(
                viewModel.tikTokLink.get().toString().trim()
            ).matches() -> {
                showToastShort(getString(R.string.err_url))
            }

            !viewModel.instagramLink.get().isNullOrBlank() && !Patterns.WEB_URL.matcher(
                viewModel.instagramLink.get().toString().trim()
            ).matches() -> {
                showToastShort(getString(R.string.err_url))
            }

            !viewModel.facebookLink.get().isNullOrBlank() && !Patterns.WEB_URL.matcher(
                viewModel.facebookLink.get().toString().trim()
            ).matches() -> {
                showToastShort(getString(R.string.err_url))
            }

            !viewModel.youtubeLink.get().isNullOrBlank() && !Patterns.WEB_URL.matcher(
                viewModel.youtubeLink.get().toString().trim()
            ).matches() -> {
                showToastShort(getString(R.string.err_url))
            }

            viewModel.description.get().isNullOrBlank() -> {
                showToastShort(getString(R.string.err_description_))
            }

            else -> {
//                viewModel.isLoading.value = true
//                viewModel.requestForPaidContentProvider()
                showCloseDialog("Congratulations you have sent a request to be Paid Content Provider.\nWe will review your request and reply as soon as possible.\nThank you!")

            }
        }
    }
}