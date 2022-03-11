package com.te6lim.ytcviewer.home.cards

import android.widget.ImageView
import androidx.core.net.toUri
import androidx.databinding.BindingAdapter
import com.bumptech.glide.Glide
import com.te6lim.ytcviewer.network.NetworkCard

@BindingAdapter("setImage")
fun ImageView.setImage(networkCard: NetworkCard?) {
    networkCard?.let {
        Glide.with(this.context).load(
            networkCard.cardImages?.get(0)?.imageUrlSmall?.toUri()
        ).into(this)
    }
}