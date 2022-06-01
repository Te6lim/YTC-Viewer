package com.te6lim.ytcviewer.filters

import android.animation.AnimatorSet
import android.animation.ObjectAnimator
import android.animation.PropertyValuesHolder
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.te6lim.ytcviewer.R
import com.te6lim.ytcviewer.databinding.ItemFilterBinding
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class FilterSelectionAdapter(
    private val callBack: CardFilterCallback
) : ListAdapter<CardFilterUiItem, CardFilterViewHolder>(DiffClass) {

    private val adapterScope = CoroutineScope(Dispatchers.Default)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CardFilterViewHolder {
        return CardFilterViewHolder.create(parent, callBack)
    }

    override fun onBindViewHolder(holder: CardFilterViewHolder, position: Int) {
        holder.bind(getItem(position))
    }

    object DiffClass : DiffUtil.ItemCallback<CardFilterUiItem>() {
        override fun areItemsTheSame(oldItem: CardFilterUiItem, newItem: CardFilterUiItem): Boolean {
            return oldItem.isSelected == newItem.isSelected
        }

        override fun areContentsTheSame(oldItem: CardFilterUiItem, newItem: CardFilterUiItem): Boolean {
            return oldItem == newItem
        }

    }

    fun submit(items: List<CardFilter>) {
        adapterScope.launch {
            submitList(items.map { filter -> CardFilterUiItem(filter) })
        }
    }
}

class CardFilterViewHolder(
    private val itemViewBinding: ItemFilterBinding,
    private val callback: CardFilterCallback
) : RecyclerView.ViewHolder(itemViewBinding.root) {

    private lateinit var animatorSet: AnimatorSet

    companion object {

        fun create(parent: ViewGroup, callback: CardFilterCallback): CardFilterViewHolder {

            val binding = DataBindingUtil.inflate<ItemFilterBinding>(
                LayoutInflater.from(parent.context), R.layout.item_filter,
                parent, false
            )

            val scaleX = PropertyValuesHolder.ofFloat(View.SCALE_X, 1.03f)
            val scaleY = PropertyValuesHolder.ofFloat(View.SCALE_Y, 1.03f)

            return CardFilterViewHolder(binding, callback).apply {

                val scaleAnimator = ObjectAnimator.ofPropertyValuesHolder(
                    itemViewBinding.filterNameContainer, scaleX, scaleY
                ).apply {
                    repeatCount = 1
                    this.repeatMode = ObjectAnimator.REVERSE
                }

                val alphaAnimator = ObjectAnimator.ofFloat(
                    itemViewBinding.filterNameContainer, View.ALPHA, 0.7f
                ).apply {
                    repeatCount = 1
                    this.repeatMode = ObjectAnimator.REVERSE
                }

                animatorSet = AnimatorSet().apply {
                    duration = 80
                    playTogether(scaleAnimator, alphaAnimator)
                }
            }
        }
    }

    fun bind(filter: CardFilterUiItem) {

        itemViewBinding.filterName.text = filter.cardFilter.name

        if (filter.isSelected) itemViewBinding.selectFilter.visibility = View.VISIBLE
        else itemViewBinding.selectFilter.visibility = View.GONE

        with(itemViewBinding.filterNameContainer) {

            setOnClickListener {
                this@CardFilterViewHolder.animate()
                if (filter.isSelected) {
                    filter.isSelected = false
                    itemViewBinding.selectFilter.visibility = View.GONE

                    callback.unSelectCardFilter(filter.cardFilter)
                } else {
                    filter.isSelected = true
                    itemViewBinding.selectFilter.visibility = View.VISIBLE

                    callback.setSelectedCardFilter(filter.cardFilter)
                }

            }
            setOnLongClickListener {
                this@CardFilterViewHolder.animate()
                true
            }
            background.setTint(callback.getColor(filter.cardFilter))
        }

        itemViewBinding.executePendingBindings()
    }

    private fun animate() {
        animatorSet.end()
        animatorSet.start()
    }

}

data class CardFilterUiItem(val cardFilter: CardFilter, var isSelected: Boolean = false)

abstract class CardFilterCallback {
    abstract fun getColor(filter: CardFilter): Int
    abstract fun setSelectedCardFilter(filter: CardFilter)
    abstract fun unSelectCardFilter(filter: CardFilter)
}
