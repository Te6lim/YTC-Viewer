package com.te6lim.ytcviewer.home.cards

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.te6lim.ytcviewer.R
import com.te6lim.ytcviewer.databinding.ItemFilterBinding

class FilterSelectionAdapter(private val callBack: CardFilterCallBack) :
    ListAdapter<String, CardFilterViewHolder>(DiffClass) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CardFilterViewHolder {
        return CardFilterViewHolder.create(parent, callBack)
    }

    override fun onBindViewHolder(holder: CardFilterViewHolder, position: Int) {
        holder.bind(getItem(position))
    }

    object DiffClass : DiffUtil.ItemCallback<String>() {
        override fun areItemsTheSame(oldItem: String, newItem: String): Boolean {
            return oldItem == newItem
        }

        override fun areContentsTheSame(oldItem: String, newItem: String): Boolean {
            return oldItem == newItem
        }

    }
}

class CardFilterViewHolder(
    private val itemViewBinding: ItemFilterBinding,
    private val cardFilterCallBack: CardFilterCallBack
) : RecyclerView.ViewHolder(itemViewBinding.root) {

    companion object {

        fun create(parent: ViewGroup, cardFilterCallBack: CardFilterCallBack)
                : CardFilterViewHolder {

            val binding = DataBindingUtil.inflate<ItemFilterBinding>(
                LayoutInflater.from(parent.context), R.layout.item_filter,
                parent, false
            )
            return CardFilterViewHolder(binding, cardFilterCallBack)
        }
    }

    fun bind(filterName: String) {

        itemViewBinding.filterNameContainer.background.setTint(
            cardFilterCallBack.getColor(filterName)
        )

        itemViewBinding.filterName.text = filterName
    }

}

class CardFilterCallBack(val cb: (String) -> Int) {
    fun getColor(filterName: String): Int {
        return cb(filterName)
    }
}