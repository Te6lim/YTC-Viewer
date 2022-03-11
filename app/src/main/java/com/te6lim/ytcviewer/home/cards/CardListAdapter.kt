package com.te6lim.ytcviewer.home.cards

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.te6lim.ytcviewer.R
import com.te6lim.ytcviewer.databinding.ItemCardBinding
import com.te6lim.ytcviewer.network.NetworkCard

class CardListAdapter : ListAdapter<NetworkCard, CardViewHolder>(DiffCallback) {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CardViewHolder {
        return CardViewHolder.create(parent)
    }

    override fun onBindViewHolder(holder: CardViewHolder, position: Int) {
        holder.bind(getItem(position))
    }
}

class CardViewHolder(
    private val itemCardBinding: ItemCardBinding
) : RecyclerView.ViewHolder(itemCardBinding.root) {

    companion object {
        fun create(parent: ViewGroup): CardViewHolder {
            val itemBinding = DataBindingUtil.inflate<ItemCardBinding>(
                LayoutInflater.from(parent.context), R.layout.item_card, parent,
                false
            )
            return CardViewHolder(itemBinding)
        }
    }

    fun bind(networkCard: NetworkCard) {

        itemCardBinding.card = networkCard
        itemCardBinding.cardName.text = networkCard.name
    }

}

object DiffCallback : DiffUtil.ItemCallback<NetworkCard>() {
    override fun areItemsTheSame(oldItem: NetworkCard, newItem: NetworkCard): Boolean {
        return oldItem == newItem
    }

    override fun areContentsTheSame(oldItem: NetworkCard, newItem: NetworkCard): Boolean {
        return oldItem.name == newItem.name
    }

}