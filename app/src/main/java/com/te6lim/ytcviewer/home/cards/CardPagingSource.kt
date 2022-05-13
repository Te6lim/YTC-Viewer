package com.te6lim.ytcviewer.home.cards

import androidx.paging.PagingSource
import androidx.paging.PagingState
import com.te6lim.ytcviewer.database.DatabaseCard
import com.te6lim.ytcviewer.network.PAGE_SIZE
import com.te6lim.ytcviewer.network.toDatabaseCard

class CardPagingSource(private val sortAsc: Boolean, val callback: CardRemoteMediator.Callback) :
    PagingSource<Int,
            DatabaseCard>() {
    override fun getRefreshKey(state: PagingState<Int, DatabaseCard>): Int? {
        return state.anchorPosition?.let { position ->
            state.closestPageToPosition(position)?.prevKey?.plus(PAGE_SIZE) ?: state.closestPageToPosition(
                position
            )?.nextKey?.minus(PAGE_SIZE)
        }
    }

    override suspend fun load(params: LoadParams<Int>): LoadResult<Int, DatabaseCard> {
        return try {
            if (sortAsc) {
                val key = params.key ?: 0
                val response = callback.getNetworkCardsAsync(key)

                val prevKey = if (key == 0) null else key - PAGE_SIZE
                val nextKey = response.meta.nextPageOffset

                LoadResult.Page(response.data.toDatabaseCard(sortAsc), prevKey, nextKey)
            } else {
                var key = params.key ?: 0
                var response = callback.getNetworkCardsAsync(key)

                if (key == 0) {
                    key = response.meta.totalRows - PAGE_SIZE
                    response = callback.getNetworkCardsAsync(key)
                }

                val nextKey = if (key == 0) null else key - PAGE_SIZE

                val prevKey = response.meta.nextPageOffset

                LoadResult.Page(response.data.toDatabaseCard(sortAsc), prevKey, nextKey)
            }
        } catch (e: Exception) {
            LoadResult.Error(e)
        }
    }
}