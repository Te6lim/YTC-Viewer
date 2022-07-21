package com.te6lim.ytcviewer.favorites

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.te6lim.ytcviewer.R
import com.te6lim.ytcviewer.database.Card
import com.te6lim.ytcviewer.databinding.ItemFavoriteBinding

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

class FavoriteViewHolder(
    private val favoriteBinding: ItemFavoriteBinding, private val action: (card: Card) -> Unit
) : RecyclerView.ViewHolder(favoriteBinding.root) {
    companion object {
        fun create(parent: ViewGroup, action: (card: Card) -> Unit): FavoriteViewHolder {
            val binding: ItemFavoriteBinding = DataBindingUtil.inflate(
                LayoutInflater.from(parent.context), R.layout.item_favorite, parent, false
            )
            return FavoriteViewHolder(binding, action)
        }
    }

    fun bind(card: Card) {
        favoriteBinding.cardItem = card
        favoriteBinding.briefDescription.text = card.name
        favoriteBinding.root.setOnClickListener {
            action(card)
        }
    }
}

object DiffCallback : DiffUtil.ItemCallback<Card>() {
    override fun areItemsTheSame(oldItem: Card, newItem: Card): Boolean {
        return oldItem.networkId == newItem.networkId
    }

    override fun areContentsTheSame(oldItem: Card, newItem: Card): Boolean {
        return oldItem == newItem
    }

}