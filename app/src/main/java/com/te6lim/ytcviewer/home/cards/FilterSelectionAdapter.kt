package com.te6lim.ytcviewer.home.cards

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.te6lim.ytcviewer.R
import com.te6lim.ytcviewer.databinding.ItemFilterBinding
import com.te6lim.ytcviewer.home.CardFilter

class FilterSelectionAdapter(
    private val callBack: CardFilterCallback
) :
    ListAdapter<CardFilter, CardFilterViewHolder>(DiffClass) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CardFilterViewHolder {
        return CardFilterViewHolder.create(parent, callBack)
    }

    override fun onBindViewHolder(holder: CardFilterViewHolder, position: Int) {
        holder.bind(getItem(position))
    }

    object DiffClass : DiffUtil.ItemCallback<CardFilter>() {
        override fun areItemsTheSame(oldItem: CardFilter, newItem: CardFilter): Boolean {
            return oldItem == newItem
        }

        override fun areContentsTheSame(oldItem: CardFilter, newItem: CardFilter): Boolean {
            return oldItem == newItem
        }

    }
}

class CardFilterViewHolder(
    private val itemViewBinding: ItemFilterBinding,
    private val callback: CardFilterCallback
) : RecyclerView.ViewHolder(itemViewBinding.root) {

    companion object {

        fun create(parent: ViewGroup, callback: CardFilterCallback): CardFilterViewHolder {

            val binding = DataBindingUtil.inflate<ItemFilterBinding>(
                LayoutInflater.from(parent.context), R.layout.item_filter,
                parent, false
            )

            return CardFilterViewHolder(binding, callback)
        }
    }

    fun bind(filter: CardFilter) {

        itemViewBinding.filterNameContainer.background.setTint(
            callback.getColor(filter)
        )

        itemViewBinding.filterName.text = filter.name
    }

}

class CardFilterCallback(val cb: (CardFilter) -> Int) {
    fun getColor(filter: CardFilter): Int {
        return cb(filter)
    }
}