package com.te6lim.ytcviewer.home.cards

import androidx.paging.PagingSource
import androidx.paging.PagingState
import com.te6lim.ytcviewer.database.DatabaseCard
import com.te6lim.ytcviewer.network.CardMetaData
import com.te6lim.ytcviewer.network.PAGE_SIZE
import com.te6lim.ytcviewer.network.Response
import com.te6lim.ytcviewer.network.toDatabaseCard
import com.te6lim.ytcviewer.repository.CardRepository

class CardPagingSource(
    private val searchType: CardRepository.SearchType, private val sortAsc: Boolean, val callback: Callback
) : PagingSource<Int, DatabaseCard>() {
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
                val response = if (searchType == CardRepository.SearchType.Filter)
                    callback.getNetworkCardsAsync(key)
                else callback.getCardsBySearchAsync(key)

                val prevKey = if (key == 0) null else key - PAGE_SIZE
                val nextKey = response.meta.nextPageOffset

                callback.setIsDataEmpty(false)

                LoadResult.Page(response.data.toDatabaseCard(sortAsc), prevKey, nextKey)
            } else {
                var key = params.key ?: 0
                var response = if (searchType == CardRepository.SearchType.Filter)
                    callback.getNetworkCardsAsync(key)
                else callback.getCardsBySearchAsync(key)

                if (key == 0) {
                    key = response.meta.totalRows - PAGE_SIZE
                    response = if (searchType == CardRepository.SearchType.Filter)
                        callback.getNetworkCardsAsync(key)
                    else callback.getCardsBySearchAsync(key)
                }

                val nextKey = if (key == 0) null else key - PAGE_SIZE

                val prevKey = response.meta.nextPageOffset

                callback.setIsDataEmpty(false)

                LoadResult.Page(response.data.toDatabaseCard(sortAsc), prevKey, nextKey)
            }
        } catch (e: IndexOutOfBoundsException) {
            callback.setIsDataEmpty(true)
            LoadResult.Page(listOf(), null, null)
        } catch (e: Exception) {
            LoadResult.Error(e)
        }
    }

    interface Callback {
        suspend fun getNetworkCardsAsync(offset: Int): Response {
            return Response(
                listOf(),
                CardMetaData(
                    0, 0, 0, 0,
                    0, null, null
                )
            )
        }

        suspend fun getCardsBySearchAsync(offset: Int): Response {
            return Response(
                listOf(),
                CardMetaData(
                    0, 0, 0, 0,
                    0, null, null
                )
            )
        }

        fun setIsDataEmpty(value: Boolean)
    }
}