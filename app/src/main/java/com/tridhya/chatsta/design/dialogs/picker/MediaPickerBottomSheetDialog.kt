package com.tridhya.chatsta.design.dialogs.picker

import android.net.Uri
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.tridhya.chatsta.R
import com.tridhya.chatsta.databinding.DialogChatMediaPickerBinding
import com.tridhya.chatsta.design.BaseBottomSheetDialogFragment
import com.tridhya.chatsta.extensions.showToast
import gun0912.tedimagepicker.builder.TedImagePicker
import gun0912.tedimagepicker.builder.type.MediaType

class MediaPickerBottomSheetDialog :
    BaseBottomSheetDialogFragment(),
    View.OnClickListener {
    private lateinit var binding: DialogChatMediaPickerBinding

    companion object {
        private lateinit var onModeSelected: OnModeSelected
        private var multiImage: Boolean = false
        fun newInstance(
            listener: OnModeSelected,
            multiImage: Boolean = false,
        ): MediaPickerBottomSheetDialog {
            Companion.multiImage = multiImage
            onModeSelected = listener
            return MediaPickerBottomSheetDialog()
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View {
        binding = DialogChatMediaPickerBinding.inflate(
            layoutInflater,
            container,
            false
        )
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.tvCamera.setOnClickListener(this)
        binding.tvGallery.setOnClickListener(this)
    }

    override fun onClick(view: View) {
        when (view.id) {
            R.id.tvCamera -> {
                if (multiImage) {
                    TedImagePicker
                        .with(requireContext())
                        .mediaType(MediaType.IMAGE)
                        .max(5, "Max Limit : 5")
                        .title("Select Image")
                        .startMultiImage { uris ->
                            onModeSelected.onMediaPicked(
                                MediaType.IMAGE,
                                uris
                            )
                        }
                } else {
                    TedImagePicker
                        .with(requireContext())
                        .mediaType(MediaType.IMAGE)
                        .title("Select Image")
                        .start { uri ->
                            onModeSelected.onMediaPicked(
                                MediaType.IMAGE,
                                listOf(uri)
                            )
                        }
                }
            }

            R.id.tvGallery -> {
                if (multiImage) {
                    try {
                        TedImagePicker
                            .with(requireContext())
                            .mediaType(MediaType.VIDEO)
                            .max(5, "Max Limit : 5")
                            .title("Select Video")
                            .startMultiImage { uris ->
                                onModeSelected.onMediaPicked(
                                    MediaType.VIDEO,
                                    uris
                                )
                            }
                    } catch (e: Exception) {
                        context?.showToast("Invalid file format")
                    }
                } else {
                    try {
                        TedImagePicker
                            .with(requireContext())
                            .mediaType(MediaType.VIDEO)
                            .title("Select Video")
                            .start { uri ->
                                onModeSelected.onMediaPicked(
                                    MediaType.VIDEO,
                                    listOf(uri)
                                )
                            }
                    } catch (e: Exception) {
                        context?.showToast("Invalid file format")
                    }
                }
            }
        }
        dismissDialog()
    }

    interface OnModeSelected {
        fun onMediaPicked(type: MediaType, uris: List<Uri>)
    }
}