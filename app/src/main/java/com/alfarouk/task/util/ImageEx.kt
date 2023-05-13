package com.alfarouk.task.util

import android.widget.ImageView
import com.alfarouk.task.R
import com.alfarouk.task.util.Constants.Image_BASE
import com.bumptech.glide.Glide

fun ImageView.loadImage(url: String?) {
    when {
        null != url && url != "" && url != Image_BASE -> Glide.with(this.context)
            .load(Image_BASE + url.trim()).into(this)
        else -> Glide.with(this.context).load(R.drawable.ic_launcher_background).into(this)
    }
}