package com.te6lim.ytcviewer.home.cards

import androidx.paging.PagingSource
import androidx.paging.PagingState
import com.te6lim.ytcviewer.database.Card
import com.te6lim.ytcviewer.network.PAGE_SIZE
import com.te6lim.ytcviewer.network.Response
import com.te6lim.ytcviewer.network.toLocalCard

class CardPagingSource(
    private val sortAsc: Boolean, val callback: Callback
) : PagingSource<Int, Card>() {
    override fun getRefreshKey(state: PagingState<Int, Card>): Int? {
        return state.anchorPosition?.let { position ->
            state.closestPageToPosition(position)?.prevKey?.plus(PAGE_SIZE) ?: state.closestPageToPosition(
                position
            )?.nextKey?.minus(PAGE_SIZE)
        }
    }

    override suspend fun load(params: LoadParams<Int>): LoadResult<Int, Card> {
        return try {
            if (sortAsc) {
                val key = params.key ?: 0
                val response = callback.getNetworkCardsAsync(key)

                val prevKey = if (key == 0) null else key - PAGE_SIZE
                val nextKey = if (response.meta.nextPage == null) null else response.meta.nextPageOffset

                callback.setIsDataEmpty(false)

                LoadResult.Page(response.data.toLocalCard(sortAsc), prevKey, nextKey)
            } else {
                var key = params.key ?: 0
                var response = callback.getNetworkCardsAsync(key)

                if (key == 0) {
                    key = response.meta.totalRows - PAGE_SIZE
                    response = callback.getNetworkCardsAsync(key)
                }

                val prevKey = if (response.meta.nextPage == null) null else response.meta.nextPageOffset
                val nextKey = if (key == 0) null else key - PAGE_SIZE

                callback.setIsDataEmpty(false)

                LoadResult.Page(response.data.toLocalCard(sortAsc), prevKey, nextKey)
            }
        } catch (e: IndexOutOfBoundsException) {
            callback.setIsDataEmpty(true)
            LoadResult.Page(listOf(), null, null)
        } catch (e: Exception) {
            LoadResult.Error(e)
        }
    }

    interface Callback {
        suspend fun getNetworkCardsAsync(offset: Int): Response
        fun setIsDataEmpty(value: Boolean)
    }
}