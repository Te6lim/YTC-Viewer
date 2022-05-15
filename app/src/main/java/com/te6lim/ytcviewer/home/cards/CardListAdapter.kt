package com.te6lim.ytcviewer.home.cards

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.paging.PagingDataAdapter
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.te6lim.ytcviewer.R
import com.te6lim.ytcviewer.databinding.ItemCardBinding
import com.te6lim.ytcviewer.domain.DomainCard

class CardListAdapter(private val clickAction: (Long) -> Unit) : PagingDataAdapter<DomainCard,
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
    private val itemCardBinding: ItemCardBinding, private val clickAction: (Long) -> Unit
) : RecyclerView.ViewHolder(itemCardBinding.root) {

    companion object {
        fun create(parent: ViewGroup, clickAction: (Long) -> Unit): CardViewHolder {
            val itemBinding = DataBindingUtil.inflate<ItemCardBinding>(
                LayoutInflater.from(parent.context), R.layout.item_card, parent,
                false
            )
            return CardViewHolder(itemBinding, clickAction)
        }
    }

    fun bind(card: DomainCard) {
        itemCardBinding.card = card
        itemCardBinding.cardItem.setOnClickListener {
            clickAction(card.networkId)
        }
    }

}

object DiffCallback : DiffUtil.ItemCallback<DomainCard>() {
    override fun areItemsTheSame(oldItem: DomainCard, newItem: DomainCard): Boolean {
        return oldItem == newItem
    }

    override fun areContentsTheSame(oldItem: DomainCard, newItem: DomainCard): Boolean {
        return oldItem.id == newItem.id
    }

}