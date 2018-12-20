package com.vamsi.xchangerates.app.utils

import android.widget.ImageView
import androidx.databinding.BindingAdapter
import com.bumptech.glide.Glide
import java.util.*

object BindingAdapters {
    @JvmStatic
    @BindingAdapter("imageUrl")
    fun bindImage(view: ImageView, fileName: String) {
        val imageUrl = "flag_".plus(fileName.toLowerCase(Locale.US))
        Glide.with(view.context).load(view.context
            .resources
            .getIdentifier(imageUrl, "drawable", view.context.packageName))
            .into(view)
    }
}