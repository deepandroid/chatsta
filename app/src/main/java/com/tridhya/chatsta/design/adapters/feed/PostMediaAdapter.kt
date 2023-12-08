package com.tridhya.chatsta.design.adapters.feed

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.lifecycle.MutableLiveData
import androidx.recyclerview.widget.RecyclerView
import com.google.android.exoplayer2.ExoPlayer
import com.google.android.exoplayer2.MediaItem
import com.google.android.exoplayer2.Player
import com.tridhya.chatsta.databinding.ItemAddPostAudioBinding
import com.tridhya.chatsta.databinding.ItemAddPostPhotoBinding
import com.tridhya.chatsta.databinding.ItemAddPostVideoBinding
import com.tridhya.chatsta.extensions.gone
import com.tridhya.chatsta.extensions.visible
import com.tridhya.chatsta.model.PostMediaModel
import com.tridhya.chatsta.provider.Constants
import com.tridhya.chatsta.utils.GlideUtils

class PostMediaAdapter(
    private val list: List<PostMediaModel> = arrayListOf(),
    private val itemVisiblePosition: MutableLiveData<Int?> = MutableLiveData(null),
) :
    RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    companion object {
        const val IMAGE = 0
        const val VIDEO = 1
        const val AUDIO = 2
    }

    fun getList(): List<PostMediaModel> {
        return list
    }

    override fun getItemViewType(position: Int): Int {
        return when (list[position].type) {
            Constants.IMAGE -> {
                IMAGE
            }
            Constants.VIDEO -> {
                VIDEO
            }
            Constants.AUDIO -> {
                AUDIO
            }
            else -> {
                IMAGE
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return when (viewType) {
            IMAGE -> {
                val binding = ItemAddPostPhotoBinding.inflate(
                    LayoutInflater.from(parent.context),
                    parent,
                    false
                )
                ImageViewHolder(binding)
            }
            VIDEO -> {
                val binding = ItemAddPostVideoBinding.inflate(
                    LayoutInflater.from(parent.context),
                    parent,
                    false
                )
                VideoViewHolder(binding, parent.context, itemVisiblePosition)
            }
            AUDIO -> {
                val binding = ItemAddPostAudioBinding.inflate(
                    LayoutInflater.from(parent.context),
                    parent,
                    false
                )
                AudioViewHolder(binding, parent.context)
            }
            else -> {
                val binding = ItemAddPostPhotoBinding.inflate(
                    LayoutInflater.from(parent.context),
                    parent,
                    false
                )
                ImageViewHolder(binding)
            }
        }
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        when (holder) {
            is ImageViewHolder -> {
                holder.bind(list[position])
                holder.binding.ivDeletePhoto.gone()
            }
            is VideoViewHolder -> {
                holder.bind(list[position])
                holder.binding.ivDeletePhoto.gone()
            }
            is AudioViewHolder -> {
                holder.bind(list[position])
                holder.binding.ivDelete.gone()
            }
        }

    }

    override fun getItemCount(): Int {
        return list.size
    }

    class AudioViewHolder(val binding: ItemAddPostAudioBinding, val context: Context) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(mediaUris: PostMediaModel) {

        }
    }

    class ImageViewHolder(val binding: ItemAddPostPhotoBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(mediaUris: PostMediaModel) {
            GlideUtils(binding.imageView.context).loadImage(mediaUris.url, binding.imageView)
        }
    }

    class VideoViewHolder(
        val binding: ItemAddPostVideoBinding, val context: Context,
        private val itemVisiblePosition: MutableLiveData<Int?> = MutableLiveData(null),
    ) :
        RecyclerView.ViewHolder(binding.root) {
        private var player: ExoPlayer? = null

        init {
            player = ExoPlayer.Builder(context).build()
            binding.videoView.player = player
            binding.videoView.useController = true
        }

        fun bind(mediaUris: PostMediaModel) {

            itemVisiblePosition.observeForever {
                if (it == bindingAdapterPosition) {
                    binding.imageView.gone()
                    binding.tvTimer.gone()
                    binding.videoView.visible()
                    playPlayer()
                } else {
                    binding.tvTimer.text = mediaUris.videoDuration
                    binding.tvTimer.visible()
                    binding.imageView.visible()
                    binding.videoView.gone()
                    pausePlayer()
                }
            }

            GlideUtils(context).loadImage(mediaUris.thumbnailUrl, binding.imageView)

            val mediaItem = mediaUris.url?.let { MediaItem.fromUri(it) }
            mediaItem?.let { player?.setMediaItem(it) }
            player?.prepare()
            player?.play()
            playPlayer()

            player?.addListener(object : Player.Listener {
                override fun onPlayerStateChanged(playWhenReady: Boolean, playbackState: Int) {
                    if (playbackState == Player.STATE_ENDED) {
                        player!!.seekTo(0)
                        player!!.playWhenReady = false
                    }
                }
            })

            binding.imageView.gone()
            binding.videoView.visible()
        }

        private fun playPlayer() {
            if (player != null) {
                player!!.playWhenReady = true
            }
        }

        private fun pausePlayer() {
            if (player != null) {
                player!!.playWhenReady = false
            }
        }

        private fun seekTo(positionInMS: Long) {
            if (player != null) {
                player!!.seekTo(positionInMS)
            }
        }

        private fun releasePlayer() {
            if (player != null) {
                player!!.release()
            }
        }
    }
}