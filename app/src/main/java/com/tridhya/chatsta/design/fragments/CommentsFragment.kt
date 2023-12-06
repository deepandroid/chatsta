package com.tridhya.chatsta.design.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.OnBackPressedCallback
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.tridhya.chatsta.Model.response.CommentsModel
import com.tridhya.chatsta.R
import com.tridhya.chatsta.databinding.FragmentCommentsBinding
import com.tridhya.chatsta.design.dialogs.MessageDialog
import com.tridhya.chatsta.design.dialogs.feed.CommentsMoreOptionsBottomDialog
import com.tridhya.chatsta.extensions.gone
import com.tridhya.chatsta.extensions.visible
import com.tridhya.chatsta.utils.GlideUtils

class CommentsFragment : BaseFragment(), View.OnClickListener,
    CommentsMoreOptionsBottomDialog.OptionSelectedListener {

    private lateinit var binding: FragmentCommentsBinding
    var postId: String? = null
    var commentId: String? = null
    var position: Int? = null
    private var replyPosition: Int? = null
    private var isReplying = false
    private var isEditing = false
    var totalCommentCount: Int? = 0
    private var deleteBtn: View? = null
    private val uriList: ArrayList<String> = ArrayList()
    private val urlList: ArrayList<String> = ArrayList()

    //    private val viewModel by lazy { FeedViewModel(requireContext()) }
//    private var commentsAdapter: CommentsAdapter? = null
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View {
        if (!this::binding.isInitialized) {
            binding = FragmentCommentsBinding.inflate(inflater, container, false)
            val arguments = arguments
            if (arguments != null) {
                postId = arguments.getString("postId")
                position = arguments.getInt("position")
            }
            initViews()
//            viewModel.isLoading.value = true
            setObservers()
            binding.rvComments.gone()
            binding.llComments.gone()
            binding.tvNoCommentsFound.visible()
//            postId?.let { viewModel.getUserComments(it) }
        }
//        binding.viewModel = viewModel
        return binding.root
    }

    private fun initViews() {
        photoAdded.observe(viewLifecycleOwner) {
            if (it == true) {
                binding.cvPhoto.visible()
                deleteBtn?.visible()
            } else {
                binding.cvPhoto.gone()
                deleteBtn?.gone()
            }
            deleteBtn = null
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
                        binding.cvPhoto.gone()
                        it.gone()
                        binding.imageView.tag = null
                        binding.imageView.setImageDrawable(null)
                    }
                })
                .show()
        }

//        commentsAdapter = CommentsAdapter(arrayListOf(), this)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val callback: OnBackPressedCallback =
            object : OnBackPressedCallback(true) {
                override fun handleOnBackPressed() {
                    findNavController().previousBackStackEntry!!
                        .savedStateHandle.set<Int>("count", totalCommentCount)
                    findNavController().previousBackStackEntry!!
                        .savedStateHandle.set<Int>("postPosition", position)
                    findNavController().navigateUp()
                }
            }

        requireActivity().onBackPressedDispatcher.addCallback(this, callback)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.ivClose.setOnClickListener(this)
        binding.ivAddMedia.setOnClickListener(this)
        binding.ivPostComments.setOnClickListener(this)
        binding.ivClear.setOnClickListener(this)
        binding.rvComments.layoutManager = LinearLayoutManager(requireContext())
    }

    private fun setObservers() {
        /*        viewModel.responseUserComments.observe(viewLifecycleOwner) {
                    viewModel.isLoading.value = false
                    if (it.comment.isNullOrEmpty()) {
                        binding.rvComments.gone()
                        binding.llComments.gone()
                        binding.tvNoCommentsFound.visible()
                    } else {
                        binding.rvComments.visible()
                        binding.llComments.visible()
                        setDonationAndCount(it.totalcount)
                        binding.tvNoCommentsFound.gone()
                        commentsAdapter?.setList(it.comment)
                        binding.rvComments.adapter = commentsAdapter
                        binding.rvComments.scrollToPosition((it.comment as ArrayList<CommentsModel>).size - 1)
                    }
                }*/

        /*        viewModel.responsePostUserComments.observe(viewLifecycleOwner) {
                    viewModel.isLoading.value = false
                    viewModel.commentText.set("")
                    commentsAdapter?.clearList()
                    binding.imageView.tag = null
                    viewModel.images.clear()
                    photoAdded.value = false
                    binding.rvComments.adapter = null
                    viewModel.isLoading.value = true
                    postId?.let { it1 -> viewModel.getUserComments(it1) }
                }

                viewModel.responseDeleteComments.observe(viewLifecycleOwner) { it ->
                    viewModel.isLoading.value = false
                    if (it != null) {
                        showToastShort(it)
                        viewModel.isLoading.value = true
                        postId?.let { viewModel.getUserComments(it) }
                        viewModel.responseDeleteComments.value = null
                    }
                }

                viewModel.responseReportPosts.observe(viewLifecycleOwner) {
                    viewModel.isLoading.value = false
                    if (it != null) {
                        showToastShort(it)
                        viewModel.responseReportPosts.value = null
                    }
                }

                viewModel.responseUserReplyComments.observe(viewLifecycleOwner) {
                    viewModel.isLoading.value = false
                    if (it != null) {
                        isReplying = false
                        commentId = null
                        photoAdded.value = false
                        binding.evComments.hint = getString(R.string.write_a_comment)
                        binding.evComments.setText("")
                        binding.imageView.tag = null
                        binding.ivClear.gone()
                        commentsAdapter?.updateItem(replyPosition, it)
                        viewModel.responseUserReplyComments.value = null
                    }
                }

                viewModel.responseDeleteCommentReply.observe(viewLifecycleOwner) {
                    viewModel.isLoading.value = false
                    if (it != null) {
                        commentsAdapter?.updateItem(replyPosition, it)
                        viewModel.responseDeleteCommentReply.value = null
                    }
                }

                viewModel.responseEditUserComments.observe(viewLifecycleOwner) {
                    viewModel.isLoading.value = false
                    if (it != null) {
                        isEditing = false
                        commentId = null
                        photoAdded.value = false
                        binding.evComments.hint = getString(R.string.write_a_comment)
                        binding.evComments.setText("")
                        binding.imageView.tag = null
                        commentsAdapter?.updateItem(replyPosition, it)
                        viewModel.responseEditUserComments.value = null
                    }
                }*/
    }

    private fun setDonationAndCount(totalCount: Int?) {
        binding.tvCommentsCount.text = "$totalCount / 50"
        totalCommentCount = totalCount
//        binding.tvCommentDonation.text = "Comments Donation is $${it?.donation}"
        binding.tvCommentDonation.text = "Comments Donation is $0"
    }

    override fun onClick(view: View?) {
        when (view?.id) {
            R.id.ivClose -> {
                preventDoubleClick(view)
                findNavController().previousBackStackEntry!!
                    .savedStateHandle.set<Int>("count", totalCommentCount)
                findNavController().previousBackStackEntry!!
                    .savedStateHandle.set<Int>("postPosition", position)
                findNavController().navigateUp()
            }

            R.id.ivPostComments -> {
                preventDoubleClick(view)
                /*if (isReplying) {
                    if (binding.imageView.tag != null || viewModel.images.isNotEmpty())
                        uploadPhotos()
                    else {
                        if (!viewModel.commentText.get()
                                .isNullOrBlank() || !viewModel.commentText.get()
                                .isNullOrEmpty()
                        ) {
                            viewModel.isLoading.value = true
                            commentId?.let { it1 -> viewModel.getUserCommentReply(it1) }
                        } else {
                            showToastShort(getString(R.string.err_reply))
                        }
                    }
                } else if (isEditing) {
                    if (binding.imageView.tag != null || viewModel.images.isNotEmpty())
                        uploadPhotos()
                    else {
                        if (!viewModel.commentText.get()
                                .isNullOrBlank() || !viewModel.commentText.get()
                                .isNullOrEmpty()
                        ) {
                            viewModel.isLoading.value = true
                            commentId?.let { it1 -> viewModel.editUserComments(it1) }
                        } else {
                            showToastShort(getString(R.string.err_comment))
                        }
                    }
                }
                else {
                    if (binding.imageView.tag != null || viewModel.images.isNotEmpty())
                        uploadPhotos()
                    else {
                        if (!viewModel.commentText.get()
                                .isNullOrBlank() || !viewModel.commentText.get()
                                .isNullOrEmpty()
                        ) {
                            viewModel.isLoading.value = true
                            postId?.let { it1 -> viewModel.postUserComments(it1) }
                        } else {
                            showToastShort(getString(R.string.err_comment))
                        }
                    }
                }*/

            }

            R.id.ivAddMedia -> {
                preventDoubleClick(view)
                openGallery(binding.imageView)
                deleteBtn = binding.ivDeletePhoto
            }

            R.id.ivClear -> {
                preventDoubleClick(view)
                isReplying = false
                commentId = null
                photoAdded.value = null
                binding.evComments.hint = getString(R.string.write_a_comment)
                binding.evComments.setText("")
                binding.imageView.tag = null
                binding.ivClear.gone()
            }
        }
    }

    private fun uploadPhotos() {
        uriList.clear()
        urlList.clear()
//        viewModel.images.clear()

        if (binding.imageView.tag != null) {
            /*val path1: String? =
                FileUtils(requireContext()).getPath(Uri.parse(binding.imageView.tag.toString()))
            Log.e("Path1", path1.toString())
            uriList.add(path1!!)*/
        }

        /*for (i in uriList)
            AWSUtils(
                requireContext(),
                i,
                this,
                AwsConstants.folderPath(session?.user, AwsConstants.IMAGE)
            ).beginUpload()*/
    }

    override fun onEditSelected(dialog: CommentsMoreOptionsBottomDialog, postModel: CommentsModel) {
        isEditing = true
        commentId = postModel.id
        binding.evComments.requestFocus()
        binding.evComments.setText(postModel.comment)
        binding.evComments.setSelection(binding.evComments.length())
        showSoftKeyboard(binding.evComments, requireActivity())
        if (!postModel.media.isNullOrBlank()) {
            photoAdded.value = true
            GlideUtils(requireContext()).loadImage(postModel.media, binding.imageView)
        }
    }

    override fun onDeleteSelected(
        dialog: CommentsMoreOptionsBottomDialog,
        postModel: CommentsModel,
    ) {
        MessageDialog.getInstance(
            requireContext(),
            getString(R.string.delete_comment_title),
            getString(R.string.delete_comment_msg),
            R.style.DefaultThemeDialog
        )
            .setPositiveButtonText(R.string.delete)
            .setNegativeButton(getString(R.string.do_not_delete))
            .setListener(object : MessageDialog.ButtonListener {
                override fun onPositiveBtnClicked(dialog: MessageDialog) {
                    dialog.dismiss()
//                    viewModel.isLoading.value = true
//                    postModel.id?.let { viewModel.deleteComment(commentId = it) }
                }
            })
            .show()
    }

    override fun onDeleteReplySelected(
        dialog: CommentsMoreOptionsBottomDialog,
        postModel: CommentsModel,
    ) {
        MessageDialog.getInstance(
            requireContext(),
            getString(R.string.delete_reply_title),
            getString(R.string.delete_reply_msg),
            R.style.DefaultThemeDialog
        )
            .setPositiveButtonText(R.string.delete)
            .setNegativeButton(getString(R.string.do_not_delete))
            .setListener(object : MessageDialog.ButtonListener {
                override fun onPositiveBtnClicked(dialog: MessageDialog) {
                    dialog.dismiss()
//                    viewModel.isLoading.value = true
                    /*postModel.id?.let {
                        postModel.replies?.get(0)?.id?.let { it1 ->
                            viewModel.deleteCommentReply(
                                commentId = it,
                                it1
                            )
                        }
                    }*/
                }
            })
            .show()
    }

    override fun onReportSelected(
        dialog: CommentsMoreOptionsBottomDialog,
        postModel: CommentsModel,
    ) {
//        viewModel.isLoading.value = true
//        postModel.id?.let { viewModel.reportPost(postId = it, reportType = COMMENTS_TYPE) }
    }
}