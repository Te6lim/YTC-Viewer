package com.te6lim.ytcviewer.home.cards

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

class FilterSelectionAdapter(
    private val callBack: CardFilterCallback
) :
    ListAdapter<CardFilter, CardFilterViewHolder>(DiffClass) {

    var prevSelectedViewHolder: CardFilterViewHolder? = null

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CardFilterViewHolder {
        return CardFilterViewHolder.create(parent, callBack)
    }

    override fun onBindViewHolder(holder: CardFilterViewHolder, position: Int) {
        holder.bind(getItem(position), position)
    }

    object DiffClass : DiffUtil.ItemCallback<CardFilter>() {
        override fun areItemsTheSame(oldItem: CardFilter, newItem: CardFilter): Boolean {
            return oldItem == newItem
        }

        override fun areContentsTheSame(oldItem: CardFilter, newItem: CardFilter): Boolean {
            return oldItem.isSelected == newItem.isSelected
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

    fun bind(filter: CardFilter, position: Int) {

        itemViewBinding.filterName.text = filter.name

        filter.position = position

        CardFilter.previousSelectedFilter?.let { f ->
            if (position == f.position) {
                (bindingAdapter as FilterSelectionAdapter).prevSelectedViewHolder = this
            }
        }

        if (filter.isSelected) itemViewBinding.selectFilter.visibility = View.VISIBLE
        else itemViewBinding.selectFilter.visibility = View.GONE

        with(itemViewBinding.filterNameContainer) {

            setOnClickListener {
                this@CardFilterViewHolder.animate()
                if (filter.isSelected) {
                    filter.isSelected = false
                    itemViewBinding.selectFilter.visibility = View.GONE

                    CardFilter.previousSelectedFilter = null
                    (bindingAdapter as FilterSelectionAdapter).prevSelectedViewHolder = null
                } else {
                    filter.isSelected = true
                    itemViewBinding.selectFilter.visibility = View.VISIBLE
                    CardFilter.previousSelectedFilter?.isSelected = false

                    (bindingAdapter as FilterSelectionAdapter).prevSelectedViewHolder?.let {
                        (bindingAdapter as FilterSelectionAdapter).onBindViewHolder(
                            it, CardFilter.previousSelectedFilter!!.position
                        )
                    }

                    CardFilter.previousSelectedFilter = filter
                    (bindingAdapter as FilterSelectionAdapter).prevSelectedViewHolder =
                        this@CardFilterViewHolder
                }

            }
            setOnLongClickListener {
                this@CardFilterViewHolder.animate()
                true
            }
            background.setTint(callback.getColor(filter))
        }

        itemViewBinding.executePendingBindings()
    }

    private fun animate() {
        animatorSet.end()
        animatorSet.start()
    }

}

class CardFilterCallback(val cb: (CardFilter) -> Int) {
    fun getColor(filter: CardFilter): Int {
        return cb(filter)
    }
}