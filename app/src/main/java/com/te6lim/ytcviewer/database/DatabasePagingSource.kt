package com.te6lim.ytcviewer.database

import androidx.paging.PagingSource
import androidx.paging.PagingState
import com.te6lim.ytcviewer.domain.DomainCard
import com.te6lim.ytcviewer.repository.Callback
import com.te6lim.ytcviewer.repository.CardRepository.Companion.PAGE_SIZE
import com.te6lim.ytcviewer.repository.CardType

class DatabasePagingSource(private val cardDb: CardDatabase, private val callback: Callback) :
    PagingSource<Int, DomainCard>() {
    override fun getRefreshKey(state: PagingState<Int, DomainCard>): Int? {
        return state.anchorPosition?.let {
            state.closestPageToPosition(it)?.prevKey?.plus(PAGE_SIZE)
                ?: state.closestPageToPosition(it)?.nextKey?.minus(PAGE_SIZE)
        }
    }

    override suspend fun load(params: LoadParams<Int>): LoadResult<Int, DomainCard> {
        val top = params.key ?: 0
        val bottom = top + PAGE_SIZE
        return try {

            val cards = if (callback.getCardListType() == CardType.MONSTER)
                cardDb.monsterDao.getAllInRange(top, bottom)?.toDomainMonsterCards()
            else cardDb.nonMonsterDao.getAllInRange(top, bottom)?.toDomainNonMonsterCards()

            val nextKey = if (cards.isNullOrEmpty()) null else params.loadSize
            val prevKey = if (top == 0) null else params.loadSize - PAGE_SIZE

            LoadResult.Page(data = cards ?: listOf(), nextKey = nextKey, prevKey = prevKey)

        } catch (e: Exception) {
            LoadResult.Error(e)
        }
    }

}