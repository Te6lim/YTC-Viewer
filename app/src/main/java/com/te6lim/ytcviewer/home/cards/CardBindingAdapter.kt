package com.te6lim.ytcviewer.home.cards

import android.widget.ImageView
import androidx.core.net.toUri
import androidx.databinding.BindingAdapter
import com.bumptech.glide.Glide
import com.te6lim.ytcviewer.database.Card

@BindingAdapter("setImage")
fun ImageView.setImage(card: Card?) {
    card?.let {
        Glide.with(this.context).load(
            card.cardImages?.get(0)?.imageUrlSmall?.toUri()
        ).into(this)
    }
}