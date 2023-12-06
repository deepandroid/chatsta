package com.tridhya.chatsta.design.fragments

import android.annotation.SuppressLint
import android.app.Activity
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.view.LayoutInflater
import android.view.MotionEvent
import android.view.View
import android.view.ViewGroup
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.StaggeredGridLayoutManager
import com.tridhya.chatsta.design.dialogs.picker.MediaPickerBottomSheetDialog
import com.tridhya.chatsta.Model.response.PostModel
import com.tridhya.chatsta.R
import com.tridhya.chatsta.databinding.FragmentCreatePostBinding
import com.tridhya.chatsta.utils.FileUtils
import com.yalantis.ucrop.UCrop
import gun0912.tedimagepicker.builder.type.MediaType
import java.io.File


class AddPostFragment : BaseFragment(), View.OnClickListener,
    MediaPickerBottomSheetDialog.OnModeSelected {

    private var postModel: PostModel? = null
    private lateinit var binding: FragmentCreatePostBinding
//    private val viewModel by lazy { FeedViewModel(requireContext()) }
//    private val mediaList: ArrayList<MediaUris> = arrayListOf()
//    private val mediaAdapter by lazy { AddPostMediaAdapter(mediaList) }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View {
        binding = FragmentCreatePostBinding.inflate(inflater, container, false)
        binding.lifecycleOwner = viewLifecycleOwner
//        binding.viewModel = viewModel
        return binding.root
    }

    @SuppressLint("ClickableViewAccessibility")
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.recyclerview.layoutManager =
            StaggeredGridLayoutManager(2, LinearLayoutManager.VERTICAL)
//        binding.recyclerview.adapter = mediaAdapter
        if (arguments?.containsKey("postId") == true) {
            binding.tvTitle.text = getString(R.string.edit_post)
            setData()
        }
        binding.ivGallery.setOnClickListener(this)
        binding.ivClose.setOnClickListener(this)
        binding.btnPost.setOnClickListener(this)

        binding.evText.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
            }

            override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
            }

            override fun afterTextChanged(p0: Editable?) {
                binding.btnPost.isEnabled = p0.toString().isNotEmpty()
            }
        })

        binding.evText.setOnTouchListener { view, event ->
            if (view.id == R.id.evText) {
                view.parent.requestDisallowInterceptTouchEvent(true)
                when (event.action and MotionEvent.ACTION_MASK) {
                    MotionEvent.ACTION_UP -> view.parent.requestDisallowInterceptTouchEvent(false)
                }
            }
            false
        }
    }

    private fun setData() {
        postModel = arguments?.getSerializable("postId") as PostModel
        binding.btnPost.isEnabled = true
//        viewModel.postText.set(postModel?.text)
        /* if (postModel?.postType == Constants.MEDIA && !postModel?.medias.isNullOrEmpty()) {
             for (media in postModel?.medias!!) {
                 mediaList.add(
                     MediaUris(
                         mediaType = if (media.type == Constants.IMAGE) com.chatsta.model.feed.MediaType.IMAGE
                         else*//* if (media.type == Constants.VIDEO)*//* com.chatsta.model.feed.MediaType.VIDEO,
                        uri = null,
                        url = media.url,
                        thumbnailUrl = media.thumbnailUrl,
                        duration = media.videoDuration
                    )
                )
            }
        }*/
    }

    override fun onClick(view: View?) {
        when (view?.id) {
            R.id.ivGallery -> {
                activity?.supportFragmentManager?.let {
                    MediaPickerBottomSheetDialog.newInstance(this).show(
                        it, "MediaPicker"
                    )
                }
            }

            R.id.btnPost -> {
//                viewModel.mediaList.clear()
                uploadMedia()
            }

            R.id.ivClose -> {
                findNavController().navigateUp()
            }
        }
    }

    private fun uploadMedia() {
        /*viewModel.isLoading.value = true
        if (mediaAdapter.getList().isNotEmpty()) {
            val mediaUris = arrayListOf<Uri>()
            for (model in mediaAdapter.getList()) {
                if (model.url.isNullOrBlank()) {
                    model.uri?.let { mediaUris.add(it) }
                    AWSUtils(
                        context = requireContext(),
                        filePath = FileUtils(requireContext()).getPath(model.uri!!).toString(),
                        onAwsImageUploadListener = this,
                        filePathKey = AwsConstants.folderPath(
                            session?.user,
                            if (model.mediaType == com.chatsta.model.feed.MediaType.VIDEO) AwsConstants.VIDEO else AwsConstants.IMAGE
                        ),
                        type = model.mediaType,
                        getVideoThumbnail = model.mediaType == com.chatsta.model.feed.MediaType.VIDEO,
                        duration = model.duration.toString()
                    ).beginUpload()
                } else {
                    viewModel.mediaList.add(
                        PostMediaModel(
                            type = if (model.mediaType == com.chatsta.model.feed.MediaType.VIDEO) Constants.VIDEO
                            else *//*if(model.mediaType = com.chatsta.model.feed.MediaType.IMAGE) *//* Constants.IMAGE,
                            url = model.url,
                            thumbnailUrl = model.thumbnailUrl,
                            videoDuration = model.duration
                        )
                    )
                }
            }
            if (mediaUris.isEmpty() && mediaList.size == mediaAdapter.getList().size) {
                createPost()
            }

        } else {
            if (!viewModel.postText.get().isNullOrBlank()) {
                createPost()
            } else {
                viewModel.isLoading.value = false
                showToastShort(getString(R.string.err_create_post))
            }
        }*/
    }

    override fun onMediaPicked(type: MediaType, uriList: List<Uri>) {
        val uris = uriList[0]
        if (type == MediaType.IMAGE) {
            val destinationUri =
                Uri.fromFile(
                    File(
                        requireContext().cacheDir,
                        "IMG_" + System.currentTimeMillis()
                    )
                )
            UCrop.of(uris, destinationUri!!)
                .withAspectRatio(1f, 1f)
                .withMaxResultSize(1080, 768)
                .start(requireContext(), this)
        } else if (type == MediaType.VIDEO) {
            val size = FileUtils(requireContext()).getFileSize(uris)
            if (size != null && size <= 30000000) {
                binding.btnPost.isEnabled = true
                /*mediaList.add(
                    MediaUris(
                        when (type) {
                            MediaType.IMAGE -> {
                                com.chatsta.model.feed.MediaType.IMAGE
                            }

                            MediaType.VIDEO -> {
                                com.chatsta.model.feed.MediaType.VIDEO
                            }
                        }, uris
                    )
                )*/
//                mediaAdapter.notifyDataSetChanged()
            } else {
                binding.btnPost.isEnabled =
                    !(binding.evText.text.isNullOrEmpty() || binding.evText.text.isNullOrBlank())
                showToastShort(getString(R.string.ett_file_size))
            }
        } else {
            binding.btnPost.isEnabled =
                !(binding.evText.text.isNullOrEmpty() || binding.evText.text.isNullOrBlank())
            showToastShort(getString(R.string.ett_file_size))
        }
    }


    private fun setObservers() {
        /*  viewModel.responseAddPost.observe(viewLifecycleOwner) {
              viewModel.isLoading.value = false
              if (it != null) {
                  findNavController().navigate(R.id.to_feed)
                  viewModel.responseAddPost.value = null
              }
          }*/
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == UCrop.REQUEST_CROP) {
            if (resultCode == Activity.RESULT_OK) {
                val uri = data?.let { UCrop.getOutput(it) }
                val size = uri?.let { FileUtils(requireContext()).getFileSize(it) }
                if (size != null && size <= 30000000) {
                    binding.btnPost.isEnabled = true
                    /*mediaList.add(
                        MediaUris(com.chatsta.model.feed.MediaType.IMAGE, uri)
                    )
                    mediaAdapter.notifyDataSetChanged()*/
                }
            } else {
                Log.e("Ucrop Error", data?.let { UCrop.getError(it)?.printStackTrace() }.toString())
            }
        }
    }
}