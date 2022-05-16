package com.te6lim.ytcviewer.details

import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.core.net.toUri
import androidx.databinding.BindingAdapter
import com.bumptech.glide.Glide
import com.te6lim.ytcviewer.R
import com.te6lim.ytcviewer.domain.Card

@BindingAdapter("setLargeImage")
fun ImageView.setImage(url: String?) {
    url?.let { Glide.with(context).load(url.toUri()).into(this) }
}

@BindingAdapter("setCardAttributeOrArchetype")
fun TextView.setCardAttributeOrArchetypeText(card: Card?) {
    card?.let {
        if (card.isNonMonsterCard()) text = card.archetype
        else setCardRaceOrAttribute(card.attribute)
    }
}

@BindingAdapter("setCardText")
fun TextView.setCardText(string: String?) {
    string?.let {
        text = string
    }
}

@BindingAdapter("setCardLevel")
fun TextView.setCardLevel(string: String?) {
    string?.let {
        text = context.getString(R.string.level, string)
    }
}

@BindingAdapter("setCardRaceOrAttribute")
fun TextView.setCardRaceOrAttribute(string: String?) {
    string?.let {
        text = context.getString(R.string.race, string)
    }
}

@BindingAdapter("setPropertiesVisibility")
fun ViewGroup.setVisibility(isNonMonsterCard: Boolean?) {
    isNonMonsterCard?.let {
        visibility = if (isNonMonsterCard) View.GONE else View.VISIBLE
    }
}