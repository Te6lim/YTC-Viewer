package com.te6lim.ytcviewer.cardList

import android.view.ViewGroup
import androidx.paging.LoadState
import androidx.paging.LoadStateAdapter

class CardDataLoadStateAdapter(private val retry: () -> Unit) : LoadStateAdapter<RetryButtonViewHolder>() {
    override fun onBindViewHolder(holder: RetryButtonViewHolder, loadState: LoadState) {
        holder.bind(loadState)
    }

    override fun onCreateViewHolder(parent: ViewGroup, loadState: LoadState): RetryButtonViewHolder {
        return RetryButtonViewHolder.create(parent, retry)
    }
}