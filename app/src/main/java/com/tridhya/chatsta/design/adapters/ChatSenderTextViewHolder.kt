package com.tridhya.chatsta.design.adapters

import android.content.Context
import android.os.CountDownTimer
import android.os.Handler
import android.os.Looper
import androidx.recyclerview.widget.RecyclerView
import com.tridhya.chatsta.R
import com.tridhya.chatsta.databinding.ItemChatSenderBinding
import com.tridhya.chatsta.extensions.visible
import com.tridhya.chatsta.interfaces.chat.OnChatItemClick
import com.tridhya.chatsta.model.ChatModel
import com.tridhya.chatsta.model.Message
import com.tridhya.chatsta.utils.GlideUtils
import com.tridhya.chatsta.utils.TimeStamp
import java.util.concurrent.TimeUnit

class ChatSenderTextViewHolder(
    val context: Context,
    val binder: ItemChatSenderBinding,
    val listener: OnChatItemClick? = null,
) :
    RecyclerView.ViewHolder(binder.root) {
    private var timer: CountDownTimer? = null
    fun bind(message: ChatModel) {
        try {
//            val aes = SecurePreferencesUtil(Constants.AES_KEY, Constants.AES_IV)
//            val decryptedMessage = aes.decryption(data.message)
            binder.tvChatMessage.text = message.message
        } catch (e: Exception) {
            binder.tvChatMessage.text = message.message
        }
        val time =
            TimeStamp.millisToFormat(message.chatTime.toString(), TimeStamp.SHORT_TIME_FORMAT)
        binder.tvTime.text = time

        binder.ivMessageStatus.visible()
        if(message.id == 0){
            binder.ivMessageStatus.setImageResource(R.drawable.ic_chat_yellow_border)
        }else if(message.id == 2){
            binder.ivMessageStatus.setImageResource(R.drawable.ic_app_small)
        }

        /*        when (message.status) {
                    MessageStatus.READ -> {
                        binder.ivMessageStatus.setImageResource(R.drawable.ic_app_small)
                    }

                    MessageStatus.RECEIVED -> {
                        binder.ivMessageStatus.setImageResource(R.drawable.ic_chat_yellow_border)
                    }

                    else -> {
                        binder.ivMessageStatus.setImageResource(R.drawable.ic_chat_yellow_border)
                    }
                }*/
        /*
                if (data.selfDestruct != null
                    && !data.selfDestruct.isNullOrBlank()
                    && !data.selfDestruct.isNullOrEmpty()
                    && !data.selfDestruct.equals(Constants.SELF_DESTRUCT_OFF, true)
                ) {
                    binder.tvSelfDestructTime.visible()
                    when (data.selfDestruct) {
                        Constants.SELF_DESTRUCT_5_MIN -> {
                            setTimer(data, (1000 * 60 * 5))
                        }

                        Constants.SELF_DESTRUCT_1_HOUR -> {
                            setTimer(data, (1000 * 60 * 60))
                        }

                        Constants.SELF_DESTRUCT_1_WEEK -> {
                            setTimer(data, (1000 * 60 * 60 * 24 * 7))
                        }
                    }
                } else {
                    binder.tvSelfDestructTime.gone()
                }*/

        itemView.setOnLongClickListener {
            listener?.onLongClick(message, context, binder.container, binder.ivAnimation)
            return@setOnLongClickListener true
        }
    }

    private fun setTimer(data: Message, timeDuration: Long) {
        if (timer != null) {
            timer!!.cancel()
        }
        if (System.currentTimeMillis() - data.timestamp <= timeDuration) {
            timer = object : CountDownTimer(
                timeDuration - (System.currentTimeMillis() - data.timestamp),
                1000
            ) {
                override fun onTick(duration: Long) {
                    if (timeDuration == (1000 * 60 * 60 * 24 * 7).toLong()) {
                        if (duration > (1000 * 60 * 60)) {
                            binder.tvSelfDestructTime.text =
                                "${TimeUnit.MILLISECONDS.toDays(duration) % 60}D ${
                                    (TimeUnit.MILLISECONDS.toHours(duration) - TimeUnit.DAYS.toHours(
                                        TimeUnit.MILLISECONDS.toDays(
                                            duration
                                        )
                                    ))
                                }H"
                        } else {
                            binder.tvSelfDestructTime.text = String.format(
                                "%02d:%02d",
                                (TimeUnit.MILLISECONDS.toHours(duration) - TimeUnit.DAYS.toHours(
                                    TimeUnit.MILLISECONDS.toDays(
                                        duration
                                    )
                                )),
                                (TimeUnit.MILLISECONDS.toMinutes(duration) -
                                        TimeUnit.HOURS.toMinutes(
                                            TimeUnit.MILLISECONDS.toHours(
                                                duration
                                            )
                                        )),
                                (TimeUnit.MILLISECONDS.toSeconds(duration) - TimeUnit.MINUTES.toSeconds(
                                    TimeUnit.MILLISECONDS.toMinutes(duration)
                                ))
                            )
                        }
                    } else {
                        binder.tvSelfDestructTime.text = String.format(
                            "%02d:%02d",
                            TimeUnit.MILLISECONDS.toMinutes(duration) % 60,
                            TimeUnit.MILLISECONDS.toSeconds(duration) % 60
                        )
                    }
                }

                override fun onFinish() {
                    binder.ivBurnAnimation.visible()
                    binder.tvSelfDestructTime.text = "00:00"
                    GlideUtils(context).showGIF(
                        R.raw.fire_flood_animation,
                        binder.ivBurnAnimation,
                    )
                    Handler(Looper.getMainLooper()).postDelayed({
                        listener?.onBurnListener(data)
                    }, 3000)
                }
            }.start()
        } else {
            binder.ivBurnAnimation.visible()
            binder.tvSelfDestructTime.text = "00:00"
            GlideUtils(context).showGIF(
                R.raw.fire_flood_animation,
                binder.ivBurnAnimation,
            )
            Handler(Looper.getMainLooper()).postDelayed({
                listener?.onBurnListener(data)
            }, 3000)
        }
    }
}