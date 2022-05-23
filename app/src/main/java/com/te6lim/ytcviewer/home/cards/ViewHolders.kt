package com.te6lim.ytcviewer.home.cards

import android.animation.ObjectAnimator
import android.animation.PropertyValuesHolder
import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.paging.LoadState
import androidx.recyclerview.widget.RecyclerView
import com.te6lim.ytcviewer.R
import com.te6lim.ytcviewer.database.Card
import com.te6lim.ytcviewer.databinding.ItemCardBinding
import com.te6lim.ytcviewer.databinding.RetryButtonBinding

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
        itemCardBinding.cardItem.setOnClickListener { clickAction(card) }
        itemCardBinding.executePendingBindings()
    }

}

class RetryButtonViewHolder(
    private val retryButtonBinding: RetryButtonBinding, private val retry: () -> Unit
) : RecyclerView.ViewHolder(retryButtonBinding.root) {

    companion object {
        fun create(parent: ViewGroup, retry: () -> Unit): RetryButtonViewHolder {
            val binding: RetryButtonBinding = DataBindingUtil.inflate(
                LayoutInflater.from(parent.context),
                R.layout.retry_button, parent, false
            )
            return RetryButtonViewHolder(binding, retry)
        }
    }

    private val scaleX = PropertyValuesHolder.ofFloat(View.SCALE_X, 1.03f)
    private val scaleY = PropertyValuesHolder.ofFloat(View.SCALE_Y, 1.03f)
    private val animator = ObjectAnimator.ofPropertyValuesHolder(
        retryButtonBinding.retryButton, scaleX, scaleY
    ).apply {
        repeatCount = 1
        repeatMode = ObjectAnimator.REVERSE
        duration = 80
    }

    @SuppressLint("Recycle")
    fun bind(state: LoadState) {
        retryButtonBinding.retryButton.setOnClickListener {
            animator.performAnimation()
            retry()
        }
        if (state is LoadState.Error) {
            retryButtonBinding.retryButtonContainer.visibility = View.VISIBLE
        } else retryButtonBinding.retryButtonContainer.visibility = View.GONE

        retryButtonBinding.executePendingBindings()
    }

    private fun ObjectAnimator.performAnimation() {
        end()
        start()
    }
}