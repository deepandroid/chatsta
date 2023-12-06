package com.tridhya.chatsta.design.dialogs.cmsPages

import android.annotation.SuppressLint
import android.content.Intent
import android.graphics.Bitmap
import android.net.Uri
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.tridhya.chatsta.R
import com.tridhya.chatsta.base.ActivityBase
import com.tridhya.chatsta.databinding.LayoutCmsPagesDialogBinding
import com.tridhya.chatsta.design.BaseBottomSheetDialogFragment
import com.tridhya.chatsta.design.viewModel.SettingsViewModel
import im.delight.android.webview.AdvancedWebView


class CMSPagesBottomDialog :
    BaseBottomSheetDialogFragment(), View.OnClickListener, AdvancedWebView.Listener {

    private lateinit var binding: LayoutCmsPagesDialogBinding
    private val viewModel by lazy { SettingsViewModel(requireContext()) }
    private var title: String? = null
    private var url: String? = null
    private var email: String? = null
    private var web: String? = null
    private var generalInformation: String? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setStyle(STYLE_NORMAL, R.style.TransparentBottomSheetDialogTheme)
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View {
        binding = LayoutCmsPagesDialogBinding.inflate(
            layoutInflater,
            container,
            false
        )
        binding.lifecycleOwner = viewLifecycleOwner
        val arguments = arguments
        if (arguments != null) {
            title = arguments.getString("title")
            url = arguments.getString("type")
            email = arguments.getString("email")
            web = arguments.getString("web")
            generalInformation = arguments.getString("generalInformation")
        }
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        showObservers()
        val sheetContainer = requireView().parent as? ViewGroup ?: return
        sheetContainer.layoutParams.height = ViewGroup.LayoutParams.MATCH_PARENT
        if (title.equals(getString(R.string.contact_us))) {
            binding.webView.visibility = View.GONE
            binding.nvContactUs.visibility = View.VISIBLE
            binding.tvEmail.text = email
            binding.tvWeb.text = web
            binding.tvGeneralInformation.text = generalInformation
        } else {
            binding.nvContactUs.visibility = View.GONE
            binding.webView.visibility = View.VISIBLE
        }
        binding.ivClose.setOnClickListener(this)
        binding.tvEmail.setOnClickListener(this)
        binding.tvWeb.setOnClickListener(this)
        binding.tvTitle.text = title
        url?.let { loadWebPage(it) }
    }

    private fun showObservers() {
        viewModel.isLoading.observe(viewLifecycleOwner) {
            if (it) showProgressbar()
            else hideProgressbar()
        }
    }

    private fun loadWebPage(url: String) {
        binding.webView.setListener(activity, this)
        binding.webView.setMixedContentAllowed(false)
        viewModel.isLoading.value = true
        binding.webView.loadUrl(url, false)
    }

    @SuppressLint("NewApi")
    override fun onResume() {
        super.onResume()
        binding.webView.onResume()
    }

    @SuppressLint("NewApi")
    override fun onPause() {
        binding.webView.onPause()
        super.onPause()
    }

    override fun onDestroy() {
        binding.webView.onDestroy()
        super.onDestroy()
    }

    override fun onClick(view: View) {
        when (view.id) {
            R.id.ivClose -> {
                (context as ActivityBase).preventDoubleClick(view)
                dialog?.dismiss()
            }

            R.id.tvEmail -> {
                (context as ActivityBase).preventDoubleClick(view)
                startActivity(
                    Intent.createChooser(
                        Intent(
                            Intent.ACTION_SENDTO, Uri.fromParts(
                                "mailto", email, null
                            )
                        ), "Send E-Mail"
                    )
                )
            }

            R.id.tvWeb -> {
                (context as ActivityBase).preventDoubleClick(view)
                startActivity(Intent(Intent.ACTION_VIEW, Uri.parse(web)))
            }
        }
    }

    override fun onPageStarted(url: String?, favicon: Bitmap?) {
    }

    override fun onPageFinished(url: String?) {
        viewModel.isLoading.value = false
    }

    override fun onPageError(errorCode: Int, description: String?, failingUrl: String?) {
        viewModel.isLoading.value = false
    }

    override fun onDownloadRequested(
        url: String?,
        suggestedFilename: String?,
        mimeType: String?,
        contentLength: Long,
        contentDisposition: String?,
        userAgent: String?,
    ) {
    }

    override fun onExternalPageRequest(url: String?) {
    }

    override fun onDestroyView() {
        viewModel.isLoading.value = false
        super.onDestroyView()
    }
}