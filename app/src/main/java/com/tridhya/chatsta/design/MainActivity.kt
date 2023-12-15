package com.tridhya.chatsta.design

import android.graphics.drawable.Drawable
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.view.MotionEvent
import android.view.WindowManager
import androidx.appcompat.widget.LinearLayoutCompat
import androidx.lifecycle.MutableLiveData
import androidx.navigation.NavController
import androidx.navigation.NavDestination
import androidx.navigation.findNavController
import androidx.vectordrawable.graphics.drawable.Animatable2Compat
import com.bumptech.glide.Glide
import com.bumptech.glide.load.DataSource
import com.bumptech.glide.load.engine.GlideException
import com.bumptech.glide.load.resource.gif.GifDrawable
import com.bumptech.glide.request.RequestListener
import com.bumptech.glide.request.target.Target
import com.tridhya.chatsta.R
import com.tridhya.chatsta.base.ActivityBase
import com.tridhya.chatsta.databinding.ActivityMainBinding
import com.tridhya.chatsta.extensions.gone
import com.tridhya.chatsta.extensions.hideKeyboard
import com.tridhya.chatsta.extensions.invisible
import com.tridhya.chatsta.extensions.visible
import com.tridhya.chatsta.utils.GlideUtils
import dagger.hilt.android.AndroidEntryPoint


@AndroidEntryPoint
class MainActivity : ActivityBase(), NavController.OnDestinationChangedListener {

    private lateinit var binding: ActivityMainBinding
    private var navController: NavController? = null
    private val selectedPosition: MutableLiveData<Int?> = MutableLiveData()
    private val list = arrayListOf<LinearLayoutCompat>()
    private var showNav = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        navController = findNavController(R.id.nav_host_fragment)
        navController?.addOnDestinationChangedListener(this)
        binding.lifecycleOwner = this
        binding.selectedPosition = selectedPosition.value

        list.clear()
        list.addAll(
            listOf(
                binding.llHome,
                binding.llChat,
                binding.llProfile,
                binding.llSettings,
            )
        )
        binding.llHome.isSelected = true


        navController?.addOnDestinationChangedListener { controller, destination, arguments ->
            if (destination.id == R.id.cpEmailLocation) {
                window.setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_NOTHING)
            } else {
                window.setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_PAN)
            }
        }
    }


    override fun onSupportNavigateUp(): Boolean {
        navController = findNavController(R.id.nav_host_fragment)
        return /*navController!!.navigateUp(appBarConfiguration) ||*/ super.onSupportNavigateUp()
    }

    private fun showBottomNav(visible: Boolean = true, selectPos: Int? = null) {
        selectedPosition.observeForever {
            if (it != null) {
                changePosition(it)
            }
        }
        if (selectPos != null) {
            selectedPosition.value = selectPos
        }
        if (visible) {
            binding.bottomAppBar.visible()
            binding.fab.show()
            binding.llHome.setOnClickListener {
                preventDoubleClick(it)
                selectedPosition.value = 0
                navController?.navigate(R.id.feed)
            }
            binding.llChat.setOnClickListener {
                preventDoubleClick(it)
                selectedPosition.value = 1
                navController?.navigate(R.id.messages)
            }
            binding.llProfile.setOnClickListener {
                preventDoubleClick(it)
                selectedPosition.value = 2
                navController?.navigate(R.id.profile)
            }
            binding.llSettings.setOnClickListener {
                preventDoubleClick(it)
                navController?.navigate(R.id.settingsDialog)
            }
            binding.fab.setOnClickListener {
                preventDoubleClick(it)
                navController?.navigate(R.id.addPost)
            }
        } else {
            binding.bottomAppBar.invisible()
            binding.fab.hide()
        }
    }

    private fun changePosition(pos: Int) {
        if (list.isNotEmpty()) {
            binding.tvMessageCount.isSelected = pos == 1
            for (i in list.indices) {
                list[i].isSelected = pos == i
            }
        }
    }

    override fun onDestinationChanged(
        controller: NavController,
        destination: NavDestination,
        arguments: Bundle?,
    ) {
        when (navController?.currentDestination?.id) {
            R.id.feed, R.id.search -> {
                showNav = true
                showBottomNav(true, 0)
            }

            R.id.messages -> {
                showNav = true
                showBottomNav(true, 1)
            }

            R.id.profile -> {
                showNav = true
                showBottomNav(true, 2)
            }

            else -> {
                showNav = false
                showBottomNav(false)
            }
        }
    }

    /*  override fun onBackPressed() {
          val navHostFragment: Fragment? =
              supportFragmentManager.findFragmentById(R.id.nav_host_fragment)
          if (navHostFragment?.childFragmentManager?.fragments?.get(0) == EnterNewPasswordFragment() || navHostFragment?.childFragmentManager?.fragments?.get(
                  0
              ) == PasswordUpdatedFragment()
          ) {
              goToActivityAndClearTask<MainActivity>()
          } else {
              super.onBackPressed()
          }
      }*/

    fun showGIF(url: String) {
        binding.ivGif.visible()
        window.setFlags(
            WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE,
            WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE
        )
        Glide.with(this).asGif().load(url)
            .listener(object : RequestListener<GifDrawable?> {
                override fun onResourceReady(
                    resource: GifDrawable?,
                    model: Any?,
                    target: Target<GifDrawable?>?,
                    dataSource: DataSource?,
                    isFirstResource: Boolean,
                ): Boolean {
                    resource?.setLoopCount(1)
                    resource?.registerAnimationCallback(object :
                        Animatable2Compat.AnimationCallback() {
                        override fun onAnimationEnd(drawable: Drawable) {
                            binding.ivGif.gone()
                            window.clearFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE)
                        }
                    })
                    return false
                }

                override fun onLoadFailed(
                    e: GlideException?,
                    model: Any?,
                    target: Target<GifDrawable?>?,
                    isFirstResource: Boolean,
                ): Boolean {
                    binding.ivGif.gone()
                    window.clearFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE)
                    return false
                }
            }).into(binding.ivGif)
    }

    fun showGIF(url: Int) {
        binding.ivGif.visible()
        window.setFlags(
            WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE,
            WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE
        )
        Glide.with(this).asGif().load(url)
            .listener(object : RequestListener<GifDrawable?> {
                override fun onResourceReady(
                    resource: GifDrawable?,
                    model: Any?,
                    target: Target<GifDrawable?>?,
                    dataSource: DataSource?,
                    isFirstResource: Boolean,
                ): Boolean {
                    resource?.setLoopCount(1)
                    resource?.registerAnimationCallback(object :
                        Animatable2Compat.AnimationCallback() {
                        override fun onAnimationEnd(drawable: Drawable) {
                            binding.ivGif.gone()
                            window.clearFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE)
                        }
                    })
                    return false
                }

                override fun onLoadFailed(
                    e: GlideException?,
                    model: Any?,
                    target: Target<GifDrawable?>?,
                    isFirstResource: Boolean,
                ): Boolean {
                    binding.ivGif.gone()
                    window.clearFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE)
                    return false
                }
            }).into(binding.ivGif)
    }

    fun showChatEmoji(url: String) {
        window.setFlags(
            WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE,
            WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE
        )
        binding.llEmoji.visible()
        GlideUtils(this).loadGif(url, binding.ivEmoji)
        Handler(Looper.getMainLooper()).postDelayed({
            binding.llEmoji.gone()
            binding.ivEmoji.setImageResource(0)
            window.clearFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE)
        }, 2000)
    }

    override fun dispatchTouchEvent(ev: MotionEvent?): Boolean {
        return try {
            if (currentFocus != null) {
                hideKeyboard()
            }
            super.dispatchTouchEvent(ev)
        } catch (e: java.lang.Exception) {
            true
        }
    }

    /*    override fun onNewIntent(intent: Intent?) {
            super.onNewIntent(intent)
    //        handleRedirection(intent)
        }*/
    /*
        private fun handleRedirection(intent: Intent?) {
            if (intent != null && intent.hasExtra(Constants.NOTIFICATION_TYPE)) {
                val type = intent.getStringExtra(Constants.NOTIFICATION_TYPE)
                val bundle = intent.getBundleExtra(Constants.DATA)!!
                notificationViewModel.resetNotificationCount(bundle.getString(Constants.NOTIFICATION_ID))
                if (type.equals("chat_message")) {
                } else if (type.equals(Constants.COMMENT_TYPE)) {
                    navController?.navigate(R.id.userComments, bundle)
                } else if (type.equals(Constants.REACTION_TYPE)) {
                    navController?.navigate(R.id.profile)
                } else if (type.equals(Constants.REQUEST_TYPE)) {
                    navController?.navigate(R.id.connectionRequests, bundle)
                } else if (type.equals(Constants.CHAT_TYPE) || type.equals(Constants.FIREBASE_CHAT)) {
                    navController?.navigate(R.id.chat, bundle)
                } else if (type.equals(Constants.GROUP_TYPE) || type.equals(Constants.FIREBASE_GROUP_CHAT)) {
                    val bundle2 = Bundle()
                    bundle2.putString(GROUP_ID, bundle.getString(CHAT_ID))
                    navController?.navigate(R.id.groupChat, bundle2)
                }
                viewModel.isLoading.value = false
            }
        }*/
    /*

        private val mReceiver = object : BroadcastReceiver() {
            override fun onReceive(p0: Context?, p1: Intent?) {
                session?.logout()
                goToActivityAndClearTask<MainActivity>()
            }
        }
    */

    /*    override fun onResume() {
            super.onResume()
            viewModel.getUserProfile(session?.user?.userId.toString())
            registerReceiver(mReceiver, IntentFilter(Constants.OTHER_DEVICE_SIGNIN))
        }*/
}