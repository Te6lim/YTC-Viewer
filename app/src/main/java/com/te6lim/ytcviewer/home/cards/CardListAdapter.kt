package com.te6lim.ytcviewer.home.cards

import android.view.ViewGroup
import androidx.paging.PagingDataAdapter
import androidx.recyclerview.widget.DiffUtil
import com.te6lim.ytcviewer.database.Card

class CardListAdapter(private val clickAction: (Card) -> Unit) :
    PagingDataAdapter<UiItem, CardViewHolder>(DiffCallback) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CardViewHolder {
        return CardViewHolder.create(parent, clickAction)
    }

    override fun onBindViewHolder(holder: CardViewHolder, position: Int) {
        getItem(position)?.let {
            if (it is UiItem.CardItem) holder.bind(it.card)
        }
    }
}

object DiffCallback : DiffUtil.ItemCallback<UiItem>() {
    override fun areItemsTheSame(oldItem: UiItem, newItem: UiItem): Boolean {
        return oldItem == newItem
    }

    override fun areContentsTheSame(oldItem: UiItem, newItem: UiItem): Boolean {
        return oldItem.id == newItem.id
    }

}