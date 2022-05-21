package com.te6lim.ytcviewer.home.cards

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.view.isVisible
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
        itemCardBinding.cardItem.setOnClickListener {
            clickAction(card)
        }
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

    fun bind(state: LoadState) {
        retryButtonBinding.retryButton.setOnClickListener {
            retry()
        }

        retryButtonBinding.root.isVisible = state is LoadState.Error
    }
}