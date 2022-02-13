package com.te6lim.ytcviewer.home.cards

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

    private lateinit var animator: ObjectAnimator

    companion object {

        fun create(parent: ViewGroup, callback: CardFilterCallback): CardFilterViewHolder {

            val binding = DataBindingUtil.inflate<ItemFilterBinding>(
                LayoutInflater.from(parent.context), R.layout.item_filter,
                parent, false
            )

            val scaleX = PropertyValuesHolder.ofFloat(View.SCALE_X, 1.03f)
            val scaleY = PropertyValuesHolder.ofFloat(View.SCALE_Y, 1.03f)

            return CardFilterViewHolder(binding, callback).apply {
                animator = ObjectAnimator.ofPropertyValuesHolder(
                    itemViewBinding.filterNameContainer, scaleX, scaleY
                ).apply {
                    repeatCount = 1
                    this.repeatMode = ObjectAnimator.REVERSE
                    duration = 80
                }
            }
        }
    }

    fun bind(filter: CardFilter) {
        with(itemViewBinding) {
            filterNameContainer.background.setTint(callback.getColor(filter))
            filterName.text = filter.name
            filterNameContainer.setOnClickListener { animate() }
        }
    }

    private fun animate() {
        animator.end()
        animator.start()
    }

}

class CardFilterCallback(val cb: (CardFilter) -> Int) {
    fun getColor(filter: CardFilter): Int {
        return cb(filter)
    }
}