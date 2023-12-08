package com.tridhya.chatsta.design.fragments.completeProfile

import android.net.Uri
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.fragment.findNavController
import com.tridhya.chatsta.R
import com.tridhya.chatsta.databinding.FragmentCpAddPhotosBinding
import com.tridhya.chatsta.design.dialogs.MessageDialog
import com.tridhya.chatsta.design.fragments.BaseFragment
import com.tridhya.chatsta.design.viewModel.CompleteProfileViewModel
import com.tridhya.chatsta.extensions.gone
import com.tridhya.chatsta.extensions.visible
import com.tridhya.chatsta.model.Images
import com.tridhya.chatsta.utils.FileUtils

class CPAddPhotosFragment : BaseFragment(),
    View.OnClickListener {

    private lateinit var binding: FragmentCpAddPhotosBinding
    private val viewModel by lazy { CompleteProfileViewModel(requireContext()) }
    private var deleteBtn: View? = null
    private val uriList: ArrayList<String> = ArrayList()
    var images: ArrayList<Images> = arrayListOf()

//        private val urlList: ArrayList<String> = ArrayList()
    private val urlList: HashMap<Int?, String?> = HashMap()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View {
        if (!this::binding.isInitialized) {
            binding = FragmentCpAddPhotosBinding.inflate(inflater, container, false)
            binding.lifecycleOwner = viewLifecycleOwner
            initViews()
        }
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.btnNext.setOnClickListener(this)
        binding.ivClose.setOnClickListener(this)
        binding.ivBack.setOnClickListener(this)

    }

    private fun initViews() {

        photoAdded.observe(viewLifecycleOwner) {
            if (it == true) {
                deleteBtn?.visible()
            } else {
                deleteBtn?.gone()
            }
            deleteBtn = null
        }

        binding.photoContainer.setOnClickListener {
            preventDoubleClick(it)
            openGallery(binding.imageView)
            deleteBtn = binding.ivDeletePhoto
        }
        binding.cvAddPhoto2.photoContainer.setOnClickListener {
            preventDoubleClick(it)
            openGallery(binding.cvAddPhoto2.imageView)
            deleteBtn = binding.cvAddPhoto2.ivDeletePhoto
        }
        binding.cvAddPhoto3.photoContainer.setOnClickListener {
            preventDoubleClick(it)
            openGallery(binding.cvAddPhoto3.imageView)
            deleteBtn = binding.cvAddPhoto3.ivDeletePhoto
        }
        binding.cvAddPhoto4.photoContainer.setOnClickListener {
            preventDoubleClick(it)
            openGallery(binding.cvAddPhoto4.imageView)
            deleteBtn = binding.cvAddPhoto4.ivDeletePhoto
        }
        binding.cvAddPhoto5.photoContainer.setOnClickListener {
            preventDoubleClick(it)
            openGallery(binding.cvAddPhoto5.imageView)
            deleteBtn = binding.cvAddPhoto5.ivDeletePhoto
        }
        binding.ivDeletePhoto.setOnClickListener {
            preventDoubleClick(it)
            MessageDialog.getInstance(
                requireContext(),
                getString(R.string.are_you_sure_you_want_to_delete_this_photo)
            )
                .setIcon(R.drawable.ic_delete_rounded)
                .setPositiveButtonText(R.string.delete)
                .setNegativeButton(R.string.cancel)
                .setListener(object : MessageDialog.ButtonListener {
                    override fun onPositiveBtnClicked(dialog: MessageDialog) {
                        dialog.dismiss()
                        it.gone()
                        binding.imageView.tag = null
                        binding.imageView.setImageDrawable(null)
                    }
                })
                .show()
        }
        binding.cvAddPhoto2.ivDeletePhoto.setOnClickListener {
            preventDoubleClick(it)
            MessageDialog.getInstance(
                requireContext(),
                getString(R.string.are_you_sure_you_want_to_delete_this_photo),
                R.style.DefaultThemeDialog
            )
                .setIcon(R.drawable.ic_delete_rounded)
                .setPositiveButtonText(R.string.delete)
                .setNegativeButton(R.string.cancel)
                .setListener(object : MessageDialog.ButtonListener {
                    override fun onPositiveBtnClicked(dialog: MessageDialog) {
                        dialog.dismiss()
                        it.gone()
                        binding.cvAddPhoto2.imageView.tag = null
                        binding.cvAddPhoto2.imageView.setImageDrawable(null)
                    }
                })
                .show()
        }
        binding.cvAddPhoto3.ivDeletePhoto.setOnClickListener {
            preventDoubleClick(it)
            MessageDialog.getInstance(
                requireContext(),
                getString(R.string.are_you_sure_you_want_to_delete_this_photo),
                R.style.DefaultThemeDialog
            )
                .setIcon(R.drawable.ic_delete_rounded)
                .setPositiveButtonText(R.string.delete)
                .setNegativeButton(R.string.cancel)
                .setListener(object : MessageDialog.ButtonListener {
                    override fun onPositiveBtnClicked(dialog: MessageDialog) {
                        dialog.dismiss()
                        it.gone()
                        binding.cvAddPhoto3.imageView.tag = null
                        binding.cvAddPhoto3.imageView.setImageDrawable(null)
                    }
                })
                .show()
        }
        binding.cvAddPhoto4.ivDeletePhoto.setOnClickListener {
            preventDoubleClick(it)
            MessageDialog.getInstance(
                requireContext(),
                getString(R.string.are_you_sure_you_want_to_delete_this_photo),
                R.style.DefaultThemeDialog
            )
                .setIcon(R.drawable.ic_delete_rounded)
                .setPositiveButtonText(R.string.delete)
                .setNegativeButton(R.string.cancel)
                .setListener(object : MessageDialog.ButtonListener {
                    override fun onPositiveBtnClicked(dialog: MessageDialog) {
                        dialog.dismiss()
                        it.gone()
                        binding.cvAddPhoto4.imageView.tag = null
                        binding.cvAddPhoto4.imageView.setImageDrawable(null)
                    }
                })
                .show()
        }
        binding.cvAddPhoto5.ivDeletePhoto.setOnClickListener {
            preventDoubleClick(it)
            MessageDialog.getInstance(
                requireContext(),
                getString(R.string.are_you_sure_you_want_to_delete_this_photo),
                R.style.DefaultThemeDialog
            )
                .setIcon(R.drawable.ic_delete_rounded)
                .setPositiveButtonText(R.string.delete)
                .setNegativeButton(R.string.cancel)
                .setListener(object : MessageDialog.ButtonListener {
                    override fun onPositiveBtnClicked(dialog: MessageDialog) {
                        dialog.dismiss()
                        it.gone()
                        binding.cvAddPhoto5.imageView.tag = null
                        binding.cvAddPhoto5.imageView.setImageDrawable(null)
                    }
                })
                .show()
        }
    }

    override fun onClick(view: View?) {
        when (view?.id) {
            R.id.btnNext -> {
                preventDoubleClick(view)
                if (binding.imageView.tag == null
                    && binding.cvAddPhoto2.imageView.tag == null
                    && binding.cvAddPhoto3.imageView.tag == null
                    && binding.cvAddPhoto4.imageView.tag == null
                    && binding.cvAddPhoto5.imageView.tag == null
                ) {
                    viewModel.images = arrayListOf()
//                    viewModel.isLoading.value = true
                    setObservers()
                    session?.user?.userId?.let {
//                        viewModel.updateStep3(it)
                        findNavController().navigate(R.id.to_cp_4)
                    }
                } else
                    uploadPhotos()
            }

            R.id.ivClose -> {
                preventDoubleClick(view)
                MessageDialog.getInstance(
                    requireContext(),
                    getString(R.string.skip_complete_profile_confirm_txt)
                )
                    .setIcon(R.drawable.ic_delete_rounded)
                    .setPositiveButtonText(R.string.no)
                    .setNegativeButton(R.string.yes)
                    .setListener(object : MessageDialog.ButtonListener {
                        override fun onPositiveBtnClicked(dialog: MessageDialog) {
                            dialog.dismiss()
                        }

                        override fun onNegativeBtnClicked(dialog: MessageDialog) {
                            super.onNegativeBtnClicked(dialog)
                            val bundle = Bundle()
                            bundle.putBoolean("isRegister", true)
                            findNavController().navigate(R.id.to_home, bundle)
                        }
                    })
                    .show()
            }

            R.id.ivBack -> {
                preventDoubleClick(view)
                findNavController().popBackStack()
            }

            R.id.photoContainer -> {
                preventDoubleClick(view)
            }

            R.id.ivDeletePhoto -> {
                preventDoubleClick(view)
            }
        }
    }

    private fun uploadPhotos() {
//        viewModel.isLoading.value = true
        uriList.clear()
        urlList.clear()

        if (binding.imageView.tag != null) {
            val path1: String? =
                FileUtils(requireContext()).getPath(Uri.parse(binding.imageView.tag.toString()))
            Log.e("Path1", path1.toString())
            uriList.add(path1!!)
        }

        if (binding.cvAddPhoto2.imageView.tag != null) {
            val path2: String? =
                FileUtils(requireContext()).getPath(Uri.parse(binding.cvAddPhoto2.imageView.tag.toString()))
            Log.e("Path2", path2.toString())
            uriList.add(path2!!)
        }

        if (binding.cvAddPhoto3.imageView.tag != null) {
            val path3: String? =
                FileUtils(requireContext()).getPath(Uri.parse(binding.cvAddPhoto3.imageView.tag.toString()))
            Log.e("path3", path3.toString())
            uriList.add(path3!!)
        }

        if (binding.cvAddPhoto4.imageView.tag != null) {
            val path4: String? =
                FileUtils(requireContext()).getPath(Uri.parse(binding.cvAddPhoto4.imageView.tag.toString()))
            Log.e("path4", path4.toString())
            uriList.add(path4!!)
        }

        if (binding.cvAddPhoto5.imageView.tag != null) {
            val path5: String? =
                FileUtils(requireContext()).getPath(Uri.parse(binding.cvAddPhoto5.imageView.tag.toString()))
            Log.e("path5", path5.toString())
            uriList.add(path5!!)
        }

        for (i in uriList.indices){
            images.add(Images(uriList[i],i.toString()))
        }
        val user = session?.user
        user?.images = images
        session?.user = user
        findNavController().navigate(R.id.to_cp_4)

            /*AWSUtils(
                context = requireContext(),
                filePath = uriList[i],
                onAwsImageUploadListener = this,
                filePathKey = AwsConstants.folderPath(session?.user, AwsConstants.IMAGE),
                position = i
            ).beginUpload()*/
    }

    private fun setObservers() {
        /*viewModel.responseUpdate.observe(viewLifecycleOwner) {
            if (it != null) {
                viewModel.isLoading.value = false
                session?.user = it.data
                it.data?.let { it1 -> updateFirebaseUser(it1) }
                findNavController().navigate(R.id.to_cp_4)
                viewModel.responseUpdate.value = null
            }
        }*/
    }
}