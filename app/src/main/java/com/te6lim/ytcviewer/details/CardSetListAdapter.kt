package com.te6lim.ytcviewer.details

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.te6lim.ytcviewer.R
import com.te6lim.ytcviewer.databinding.ItemSetCardBinding
import com.te6lim.ytcviewer.network.CardSet

class CardSetListAdapter : ListAdapter<CardSet, CardSetViewHolder>(DiffCallback) {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CardSetViewHolder {
        return CardSetViewHolder.create(parent)
    }

    override fun onBindViewHolder(holder: CardSetViewHolder, position: Int) {
        holder.bind(getItem(position))
    }
}

class CardSetViewHolder(
    private val cardSetBinding: ItemSetCardBinding
) : RecyclerView.ViewHolder(cardSetBinding.root) {
    companion object {
        fun create(parent: ViewGroup): CardSetViewHolder {
            val binding: ItemSetCardBinding = DataBindingUtil.inflate(
                LayoutInflater.from(parent.context), R.layout.item_set_card,
                parent, false
            )
            return CardSetViewHolder(binding)
        }
    }

    fun bind(cardSet: CardSet) {
        cardSetBinding.setTitleText.text = cardSet.setName
        cardSetBinding.rarityText.text = cardSet.setRarity
        cardSetBinding.priceText.text = itemView.context.getString(
            R.string.cardSetPrice_text, cardSet.setPrice
        )
    }
}

object DiffCallback : DiffUtil.ItemCallback<CardSet>() {
    override fun areItemsTheSame(oldItem: CardSet, newItem: CardSet): Boolean {
        return oldItem == newItem
    }

    override fun areContentsTheSame(oldItem: CardSet, newItem: CardSet): Boolean {
        return oldItem.setCode == newItem.setCode
    }

}