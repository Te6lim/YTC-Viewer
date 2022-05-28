package com.te6lim.ytcviewer.home.favorites

import androidx.databinding.BindingAdapter
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.te6lim.ytcviewer.database.Card
import com.te6lim.ytcviewer.home.cards.FavoriteViewHolder

@Suppress("UNCHECKED_CAST")
@BindingAdapter("setListItems")
fun RecyclerView.setListItems(list: List<Card>?) {
    list?.let { cardList ->
        adapter?.let {
            (it as ListAdapter<Card, FavoriteViewHolder>).submitList(cardList)
        }
    }
}