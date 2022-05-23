package com.te6lim.ytcviewer.details

import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.te6lim.ytcviewer.databinding.ItemSetCardBinding
import com.te6lim.ytcviewer.network.CardSet

class CardSetListAdapter : ListAdapter<CardSet, CardSetViewHolder>(DiffCallback) {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CardSetViewHolder {
        TODO("Not yet implemented")
    }

    override fun onBindViewHolder(holder: CardSetViewHolder, position: Int) {
        TODO("Not yet implemented")
    }
}

class CardSetViewHolder(
    private val cardSetBinding: ItemSetCardBinding
) : RecyclerView.ViewHolder(cardSetBinding.root) {

}

object DiffCallback : DiffUtil.ItemCallback<CardSet>() {
    override fun areItemsTheSame(oldItem: CardSet, newItem: CardSet): Boolean {
        TODO("Not yet implemented")
    }

    override fun areContentsTheSame(oldItem: CardSet, newItem: CardSet): Boolean {
        TODO("Not yet implemented")
    }

}