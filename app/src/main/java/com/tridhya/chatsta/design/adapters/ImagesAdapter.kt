package com.tridhya.chatsta.design.adapters

import android.content.Context
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.viewpager.widget.PagerAdapter
import androidx.viewpager.widget.ViewPager
import com.tridhya.chatsta.model.Images
import com.tridhya.chatsta.utils.GlideUtils


class ImagesAdapter(val mContext: Context, private val imagesList: List<Images>) : PagerAdapter() {
    override fun getCount(): Int {
        return imagesList.size
    }

    override fun isViewFromObject(view: View, `object`: Any): Boolean {
        return view === `object` as ImageView
    }

    override fun instantiateItem(container: ViewGroup, position: Int): Any {
        val list = imagesList[position].image
        val imageView = ImageView(mContext)
        imageView.scaleType = ImageView.ScaleType.CENTER_CROP
        GlideUtils(mContext).loadImage(list, imageView)
        (container as ViewPager).addView(imageView, 0)
        return imageView
    }

    override fun destroyItem(container: ViewGroup, position: Int, `object`: Any) {
        (container as ViewPager).removeView(`object` as ImageView?)
    }

}