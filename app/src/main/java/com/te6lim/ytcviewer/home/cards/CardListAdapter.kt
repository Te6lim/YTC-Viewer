package com.te6lim.ytcviewer.home.cards

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.paging.PagingDataAdapter
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.te6lim.ytcviewer.R
import com.te6lim.ytcviewer.databinding.ItemCardBinding
import com.te6lim.ytcviewer.domain.Card

class CardListAdapter(private val clickAction: (Card) -> Unit) : PagingDataAdapter<Card,
        CardViewHolder>
    (DiffCallback) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CardViewHolder {
        return CardViewHolder.create(parent, clickAction)
    }

    override fun onBindViewHolder(holder: CardViewHolder, position: Int) {
        getItem(position)?.let { holder.bind(it) }
    }
}

class CardViewHolder(
    private val itemCardBinding: ItemCardBinding, private val clickAction: (Card) -> Unit
) : RecyclerView.ViewHolder(itemCardBinding.root) {

    companion object {
        fun create(parent: ViewGroup, clickAction: (Card) -> Unit): CardViewHolder {
            val itemBinding = DataBindingUtil.inflate<ItemCardBinding>(
                LayoutInflater.from(parent.context), R.layout.item_card, parent,
                false
            )
            return CardViewHolder(itemBinding, clickAction)
        }
    }

    fun bind(card: Card) {
        itemCardBinding.card = card
        itemCardBinding.cardItem.setOnClickListener {
            clickAction(card)
        }
    }

}

object DiffCallback : DiffUtil.ItemCallback<Card>() {
    override fun areItemsTheSame(oldItem: Card, newItem: Card): Boolean {
        return oldItem == newItem
    }

    override fun areContentsTheSame(oldItem: Card, newItem: Card): Boolean {
        return oldItem.id == newItem.id
    }

}