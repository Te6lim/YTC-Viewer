package com.te6lim.ytcviewer.home.cards

import android.animation.AnimatorSet
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
import com.te6lim.ytcviewer.databinding.ItemFavoriteBinding
import com.te6lim.ytcviewer.databinding.RetryButtonBinding

class CardViewHolder(
    private val itemCardBinding: ItemCardBinding, private val clickAction: (Card) -> Unit
) : RecyclerView.ViewHolder(itemCardBinding.root) {

    private lateinit var animatorSet: AnimatorSet

    companion object {
        fun create(parent: ViewGroup, clickAction: (Card) -> Unit): CardViewHolder {
            val itemBinding = DataBindingUtil.inflate<ItemCardBinding>(
                LayoutInflater.from(parent.context), R.layout.item_card, parent,
                false
            )
            return CardViewHolder(itemBinding, clickAction).apply {
                val scaleX = PropertyValuesHolder.ofFloat(View.SCALE_X, 1.03f)
                val scaleY = PropertyValuesHolder.ofFloat(View.SCALE_Y, 1.03f)

                val scaleAnimator = ObjectAnimator.ofPropertyValuesHolder(
                    itemCardBinding.cardItem, scaleX, scaleY
                ).apply {
                    repeatCount = 1
                    repeatMode = ObjectAnimator.REVERSE
                }

                val alphaAnimator = ObjectAnimator.ofFloat(itemBinding.cardItem, View.ALPHA, 0.6f).apply {
                    repeatCount = 1
                    repeatMode = ObjectAnimator.REVERSE
                }

                animatorSet = AnimatorSet().apply {
                    duration = 80
                    playTogether(scaleAnimator, alphaAnimator)
                }
            }
        }
    }

    fun bind(card: Card) {
        itemCardBinding.card = card
        itemCardBinding.cardItem.setOnClickListener {
            animatorSet.play()
            clickAction(card)
        }
        itemCardBinding.cardItem.setOnLongClickListener {
            animatorSet.play()
            true
        }
        itemCardBinding.executePendingBindings()
    }

    private fun AnimatorSet.play() {
        end()
        start()
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

class FavoriteViewHolder(
    private val favoriteBinding: ItemFavoriteBinding, private val action: (card: Card) -> Unit
) : RecyclerView.ViewHolder(favoriteBinding.root) {
    companion object {
        fun create(parent: ViewGroup, action: (card: Card) -> Unit): FavoriteViewHolder {
            val binding: ItemFavoriteBinding = DataBindingUtil.inflate(
                LayoutInflater.from(parent.context), R.layout.item_favorite, parent, false
            )
            return FavoriteViewHolder(binding, action)
        }
    }

    fun bind(card: Card) {
        favoriteBinding.cardItem = card
        favoriteBinding.briefDescription.text = card.name
        favoriteBinding.root.setOnClickListener {
            action(card)
        }
    }
}