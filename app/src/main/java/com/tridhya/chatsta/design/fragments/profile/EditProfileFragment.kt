package com.tridhya.chatsta.design.fragments.profile

import android.annotation.SuppressLint
import android.net.Uri
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.MotionEvent
import android.view.View
import android.view.ViewGroup
import androidx.core.widget.addTextChangedListener
import androidx.recyclerview.widget.GridLayoutManager
import com.tridhya.chatsta.R
import com.tridhya.chatsta.databinding.FragmentEditProfileBinding
import com.tridhya.chatsta.design.adapters.completeProfile.EnumDataAdapter
import com.tridhya.chatsta.design.adapters.completeProfile.EnumInterestAdapter
import com.tridhya.chatsta.design.dialogs.MessageDialog
import com.tridhya.chatsta.design.dialogs.NumberPickerBottomDialog
import com.tridhya.chatsta.design.fragments.BaseFragment
import com.tridhya.chatsta.design.viewModel.ProfileViewModel
import com.tridhya.chatsta.extensions.gone
import com.tridhya.chatsta.extensions.visible
import com.tridhya.chatsta.model.AllInterestResponseModel
import com.tridhya.chatsta.model.EnumDataModel
import com.tridhya.chatsta.model.Images
import com.tridhya.chatsta.model.User
import com.tridhya.chatsta.utils.FileUtils
import com.tridhya.chatsta.utils.GlideUtils
import com.tridhya.chatsta.utils.RelationshipStatusData

class EditProfileFragment : BaseFragment(),
    View.OnClickListener {

    private lateinit var binding: FragmentEditProfileBinding
    private val viewModel by lazy { ProfileViewModel(requireContext()) }
    private var genderList = arrayListOf<EnumDataModel>()
    private var relationshipList = arrayListOf<EnumDataModel>()
    private var starSignList = arrayListOf<EnumDataModel>()

    private lateinit var genderAdapter: EnumDataAdapter
    private lateinit var relationshipAdapter: EnumDataAdapter
    private lateinit var starSignAdapter: EnumDataAdapter
    private var interestList: ArrayList<AllInterestResponseModel> = arrayListOf()
    private var selectedInterestList: ArrayList<String> = arrayListOf()

    private lateinit var interestAdapter: EnumInterestAdapter
    private var deleteBtn: View? = null
    var user: User? = null
    private var height = 50

    private val uriList: ArrayList<String> = ArrayList()
    private val urlList: ArrayList<String> = ArrayList()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View {
        binding = FragmentEditProfileBinding.inflate(inflater, container, false)
        binding.lifecycleOwner = viewLifecycleOwner
        binding.viewModel = viewModel
        user = session?.user
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.ivClose.setOnClickListener(this)
        binding.btnOk.setOnClickListener(this)
        initViews()
        setObservers()
//        viewModel.isLoading.value = true
//        viewModel.interestList()
        binding.etHeight.editText.setOnClickListener {
            preventDoubleClick(it)
            if (binding.etHeight.value != null)
                height = binding.etHeight.value?.toInt()!!

            activity?.supportFragmentManager?.let { it1 ->
                NumberPickerBottomDialog.newInstance(
                    requireContext(),
                    height,
                    object : NumberPickerBottomDialog.OptionListener {
                        override fun onOptionSelected(view: View, selectedValue: Int) {
                            binding.etHeight.value = "$selectedValue"
                        }

                    }).show(it1, "dialog")
            }
        }
    }

    private fun setObservers() {
        /*viewModel.responseUpdate.observe(viewLifecycleOwner) {
            viewModel.isLoading.value = false
            session?.user = it.data
            it.data?.let { it1 -> updateUser(it1) }
            requireActivity().onBackPressed()
        }*/
        /*        viewModel.allInterestList.observe(viewLifecycleOwner) { allInterestList ->
                    viewModel.isLoading.value = false
                    if (user?.interests?.size!! > 0) {
                        for (i in user?.interests?.indices!!) {
                            for (j in allInterestList.indices) {
                                if (user?.interests!![i].interestId == allInterestList[j].interestId) {
                                    allInterestList[j].isSelected = true
                                }
                            }
                        }
                    }
                    interestList = allInterestList
                    interestAdapter =
                        EnumInterestAdapter(interestList, object : EnumInterestAdapter.OnItemClickListener {
                            override fun onItemSelected(data: AllInterestResponseModel) {
                                if (interestAdapter.getSelectedCount() == 0) {
                                    binding.llInterest.recyclerview.isSelected = false
                                    binding.llInterest.tvTitle.isSelected = false
                                } else {
                                    binding.llInterest.recyclerview.isSelected = true
                                    binding.llInterest.tvTitle.isSelected = true
                                }
                            }
                        }, true)
                    binding.llInterest.recyclerview.layoutManager = GridLayoutManager(context, 2)
                    binding.llInterest.recyclerview.adapter = interestAdapter
                }*/
    }

    @SuppressLint("ClickableViewAccessibility")
    private fun initViews() {
        if (user?.images?.isEmpty() == true) {
            return
        }
        if (user?.images?.size!! >= 1) {
            GlideUtils(requireContext()).loadImage(
                user?.images?.get(0)?.image,
                binding.cvAddPhoto1.imageView
            )
            binding.cvAddPhoto1.imageView.tag = user?.images?.get(0)?.image
            binding.cvAddPhoto1.ivDeletePhoto.visible()
        }
        if (user?.images?.size!! >= 2) {
            GlideUtils(requireContext()).loadImage(
                user?.images?.get(1)?.image,
                binding.cvAddPhoto2.imageView
            )
            binding.cvAddPhoto2.imageView.tag = user?.images?.get(1)?.image
            binding.cvAddPhoto2.ivDeletePhoto.visible()
        }
        if (user?.images?.size!! >= 3) {
            GlideUtils(requireContext()).loadImage(
                user?.images?.get(2)?.image,
                binding.cvAddPhoto3.imageView
            )
            binding.cvAddPhoto3.imageView.tag = user?.images?.get(2)?.image
            binding.cvAddPhoto3.ivDeletePhoto.visible()
        }
        if (user?.images?.size!! >= 4) {
            GlideUtils(requireContext()).loadImage(
                user?.images?.get(3)?.image,
                binding.cvAddPhoto4.imageView
            )
            binding.cvAddPhoto4.imageView.tag = user?.images?.get(3)?.image
            binding.cvAddPhoto4.ivDeletePhoto.visible()
        }
        if (user?.images?.size!! >= 5) {
            GlideUtils(requireContext()).loadImage(
                user?.images?.get(4)?.image,
                binding.cvAddPhoto5.imageView
            )
            binding.cvAddPhoto5.imageView.tag = user?.images?.get(4)?.image
            binding.cvAddPhoto5.ivDeletePhoto.visible()
        }
        binding.llBio.editText.setOnFocusChangeListener { view, b ->
            binding.llBio.tvTitle.isSelected = b
        }

        binding.etQuotes.editText.setOnFocusChangeListener { view, b ->
            binding.etQuotes.tvTitle.isSelected = b
        }

        binding.etQuotes.editText.setOnTouchListener { view, event ->
            if (view.id == R.id.editText) {
                view.parent.requestDisallowInterceptTouchEvent(true)
                when (event.action and MotionEvent.ACTION_MASK) {
                    MotionEvent.ACTION_UP -> view.parent.requestDisallowInterceptTouchEvent(
                        false
                    )
                }
            }
            false
        }

        binding.llBio.editText.setOnTouchListener { view, event ->
            if (view.id == R.id.editText) {
                view.parent.requestDisallowInterceptTouchEvent(true)
                when (event.action and MotionEvent.ACTION_MASK) {
                    MotionEvent.ACTION_UP -> view.parent.requestDisallowInterceptTouchEvent(
                        false
                    )
                }
            }
            false
        }

        binding.llBio.editText.addTextChangedListener {
            if (it.toString().isNotBlank()) {
                binding.llBio.editText.isSelected = true
                binding.llBio.tvTitle.isSelected = true
            }
        }
        binding.etQuotes.editText.addTextChangedListener {
            if (it.toString().isNotBlank()) {
                binding.etQuotes.editText.isSelected = true
                binding.etQuotes.tvTitle.isSelected = true
            }
        }

        binding.etHeight.editText.addTextChangedListener {
            if (it.toString().isNotBlank() && it.toString().toInt() > 0) {
                binding.etHeight.editText.isSelected = true
                binding.etHeight.tvTitle.isSelected = true
                binding.etHeight.ivDownArrow.isSelected = true
            }
        }

        genderList = RelationshipStatusData.getGenderList(requireContext())
        if (user?.gender != null) {
            for (i in genderList.indices) {
                if (user?.gender.equals(genderList[i].enum, true)) {
                    genderList[i].isSelected = true
                    viewModel.gender = genderList[i]
                }
            }
        }

        relationshipList = RelationshipStatusData.getRelationshipList(requireContext())
        if (user?.relationShipStatus != null) {
            for (i in relationshipList.indices) {
                if (user?.relationShipStatus.equals(relationshipList[i].enum, true)) {
                    relationshipList[i].isSelected = true
                    viewModel.relationshipStatus = relationshipList[i]
                }
            }
        }

        starSignList = RelationshipStatusData.getStarSignList(requireContext())
        if (user?.starSign != null) {
            for (i in starSignList.indices) {
                if (user?.starSign.equals(starSignList[i].enum, true)) {
                    starSignList[i].isSelected = true
                    viewModel.starSign = starSignList[i]
                }
            }
        }

         genderAdapter = EnumDataAdapter(
             genderList,
             object : EnumDataAdapter.OnItemClickListener {
                 override fun onItemSelected(data: EnumDataModel) {
                     binding.llGender.recyclerview.isSelected = true
                     binding.llGender.tvTitle.isSelected = true
                     viewModel.gender = data
                 }
             }
         )
        binding.llGender.recyclerview.layoutManager = GridLayoutManager(context, 3)
        binding.llGender.recyclerview.adapter = genderAdapter


        relationshipAdapter =
            EnumDataAdapter(relationshipList, object : EnumDataAdapter.OnItemClickListener {
                override fun onItemSelected(data: EnumDataModel) {
                    binding.llRelationshipStatus.recyclerview.isSelected = true
                    binding.llRelationshipStatus.tvTitle.isSelected = true
                    viewModel.relationshipStatus = data
                }
            })
        binding.llRelationshipStatus.recyclerview.layoutManager = GridLayoutManager(context, 3)
        binding.llRelationshipStatus.recyclerview.adapter = relationshipAdapter

        starSignAdapter =
            EnumDataAdapter(starSignList, object : EnumDataAdapter.OnItemClickListener {
                override fun onItemSelected(data: EnumDataModel) {
                    binding.llStarSign.recyclerview.isSelected = true
                    binding.llStarSign.tvTitle.isSelected = true
                    viewModel.starSign = data
                }
            })
        binding.llStarSign.recyclerview.layoutManager = GridLayoutManager(context, 4)
        binding.llStarSign.recyclerview.adapter = starSignAdapter

        if (!user?.gender.isNullOrEmpty()) {
            binding.llGender.llInputText.isSelected = true
            binding.llGender.tvTitle.isSelected = true
        }
        if (!user?.relationShipStatus.isNullOrEmpty()) {
            binding.llRelationshipStatus.llInputText.isSelected = true
            binding.llRelationshipStatus.tvTitle.isSelected = true
        }
        if (!user?.starSign.isNullOrEmpty()) {
            binding.llStarSign.llInputText.isSelected = true
            binding.llStarSign.tvTitle.isSelected = true
        }
        if (user?.interests?.size!! > 0) {
            binding.llInterest.llInputText.isSelected = true
            binding.llInterest.tvTitle.isSelected = true
        }
        if (!user?.bio.isNullOrEmpty()) {
            viewModel.bio.set(user?.bio)
            binding.llBio.tvTitle.isSelected = true
            binding.llBio.llInputText.isSelected = true
        }
        if (user?.height != null && user?.height != 0) {
            viewModel.height.set(user?.height.toString())
            binding.etHeight.tvTitle.isSelected = true
            binding.etHeight.editText.isSelected = true
        }
        if (!user?.quotes.isNullOrEmpty()) {
            viewModel.quotes.set(user?.quotes)
            binding.etQuotes.tvTitle.isSelected = true
            binding.etQuotes.llInputText.isSelected = true
        }

        photoAdded.observe(viewLifecycleOwner) {
            if (it == true) {
                deleteBtn?.visible()
            } else {
                deleteBtn?.gone()
            }
            deleteBtn = null
        }
        binding.cvAddPhoto1.photoContainer.setOnClickListener {
            preventDoubleClick(it)
            openGallery(binding.cvAddPhoto1.imageView)
            deleteBtn = binding.cvAddPhoto1.ivDeletePhoto
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
        binding.cvAddPhoto1.ivDeletePhoto.setOnClickListener {
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
                        binding.cvAddPhoto1.imageView.tag = null
                        binding.cvAddPhoto1.imageView.setImageDrawable(null)
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

    private fun getSelectedInterests(): ArrayList<String> {
        for (i in interestList.indices) {
            if (interestList[i].isSelected)
                interestList[i].interestId?.let { selectedInterestList.add(it) }
        }
        return selectedInterestList
    }

    override fun onClick(view: View?) {
        when (view?.id) {
            R.id.ivClose -> {
                preventDoubleClick(view)
                hideKeyboard()
                requireActivity().onBackPressed()
            }

            R.id.btnOk -> {
                preventDoubleClick(view)
                hideKeyboard()
                uploadPhotos()
            }
        }
    }

    private fun uploadPhotos() {
        urlList.clear()
//        viewModel.isLoading.value = true
        val userImages = arrayListOf<Images>()
        session?.user?.images?.let { userImages.addAll(it) }

        if (binding.cvAddPhoto1.imageView.tag is Uri) {
            val path1: String? =
                FileUtils(requireContext()).getPath(Uri.parse(binding.cvAddPhoto1.imageView.tag.toString()))
            uriList.add(path1!!)
            Log.e("Path1", path1.toString())
            /* AWSUtils(
                 requireContext(),
                 path1,
                 this,
                 AwsConstants.folderPath(session?.user, AwsConstants.IMAGE),
                 binding.cvAddPhoto1.imageView
             ).beginUpload()*/
        }

        if (binding.cvAddPhoto2.imageView.tag is Uri) {
            val path2: String? =
                FileUtils(requireContext()).getPath(Uri.parse(binding.cvAddPhoto2.imageView.tag.toString()))
            uriList.add(path2!!)
            Log.e("Path2", path2.toString())
            /* AWSUtils(
                 requireContext(),
                 path2,
                 this,
                 AwsConstants.folderPath(session?.user, AwsConstants.IMAGE),
                 binding.cvAddPhoto2.imageView
             ).beginUpload()*/
        }

        if (binding.cvAddPhoto3.imageView.tag is Uri) {
            val path3: String? =
                FileUtils(requireContext()).getPath(Uri.parse(binding.cvAddPhoto3.imageView.tag.toString()))
            Log.e("path3", path3.toString())
            uriList.add(path3!!)
            /*AWSUtils(
                requireContext(),
                path3,
                this,
                AwsConstants.folderPath(session?.user, AwsConstants.IMAGE),
                binding.cvAddPhoto3.imageView
            ).beginUpload()*/
        }

        if (binding.cvAddPhoto4.imageView.tag is Uri) {
            val path4: String? =
                FileUtils(requireContext()).getPath(Uri.parse(binding.cvAddPhoto4.imageView.tag.toString()))
            Log.e("path4", path4.toString())
            uriList.add(path4!!)
            /*AWSUtils(
                requireContext(),
                path4,
                this,
                AwsConstants.folderPath(session?.user, AwsConstants.IMAGE),
                binding.cvAddPhoto4.imageView
            ).beginUpload()*/
        }

        if (binding.cvAddPhoto5.imageView.tag is Uri) {
            val path5: String? =
                FileUtils(requireContext()).getPath(Uri.parse(binding.cvAddPhoto5.imageView.tag.toString()))
            Log.e("path5", path5.toString())
            uriList.add(path5!!)
            /*AWSUtils(
                requireContext(),
                path5,
                this,
                AwsConstants.folderPath(session?.user, AwsConstants.IMAGE),
                binding.cvAddPhoto5.imageView
            ).beginUpload()*/
        }

        if (uriList.isEmpty()) {
            viewModel.images.clear()
            getAllImages()
            session?.user?.userId?.let {
                /* viewModel.updateEditedProfile(
                     getSelectedInterests()
                 )*/
            }
        }

    }


    private fun getAllImages() {
        viewModel.images.add(
            Images(
                id = (if (!session?.user?.images.isNullOrEmpty()) session?.user?.images?.get(
                    0
                )?.id else null),
                image = if (binding.cvAddPhoto1.imageView.tag != null) binding.cvAddPhoto1.imageView.tag.toString() else null
            )
        )
        viewModel.images.add(
            Images(
                id = (if (!session?.user?.images.isNullOrEmpty() && session?.user?.images?.size!! >= 2) session?.user?.images?.get(
                    1
                )?.id else null),
                image = if (binding.cvAddPhoto2.imageView.tag != null) binding.cvAddPhoto2.imageView.tag.toString() else null
            )
        )
        viewModel.images.add(
            Images(
                id = (if (!session?.user?.images.isNullOrEmpty() && session?.user?.images?.size!! >= 3) session?.user?.images?.get(
                    2
                )?.id else null),
                image = if (binding.cvAddPhoto3.imageView.tag != null) binding.cvAddPhoto3.imageView.tag.toString() else null
            )
        )
        viewModel.images.add(
            Images(
                id = (if (!session?.user?.images.isNullOrEmpty() && session?.user?.images?.size!! >= 4) session?.user?.images?.get(
                    3
                )?.id else null),
                image = if (binding.cvAddPhoto4.imageView.tag != null) binding.cvAddPhoto4.imageView.tag.toString() else null
            )
        )
        viewModel.images.add(
            Images(
                id = (if (!session?.user?.images.isNullOrEmpty() && session?.user?.images?.size!! >= 5) session?.user?.images?.get(
                    4
                )?.id else null),
                image = if (binding.cvAddPhoto5.imageView.tag != null) binding.cvAddPhoto5.imageView.tag.toString() else null
            )
        )
        val iterator: MutableIterator<Images> = viewModel.images.iterator()
        while (iterator.hasNext()) {
            val str = iterator.next()
            if (str.id.isNullOrBlank() && str.image.isNullOrBlank()) {
                iterator.remove()
            }
        }
    }

}