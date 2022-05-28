package com.te6lim.ytcviewer.home.favorites

import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import com.te6lim.ytcviewer.database.Card
import com.te6lim.ytcviewer.home.cards.FavoriteViewHolder

class FavoritesListAdapter(
    private val action: (card: Card) -> Unit
) : ListAdapter<Card, FavoriteViewHolder>(DiffCallback) {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): FavoriteViewHolder {
        return FavoriteViewHolder.create(parent, action)
    }

    override fun onBindViewHolder(holder: FavoriteViewHolder, position: Int) {
        holder.bind(getItem(position))
    }
}

object DiffCallback : DiffUtil.ItemCallback<Card>() {
    override fun areItemsTheSame(oldItem: Card, newItem: Card): Boolean {
        return oldItem == newItem
    }

    override fun areContentsTheSame(oldItem: Card, newItem: Card): Boolean {
        return oldItem.networkId == newItem.networkId
    }

}