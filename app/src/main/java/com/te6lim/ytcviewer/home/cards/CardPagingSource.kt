package com.te6lim.ytcviewer.home.cards

import androidx.paging.PagingSource
import androidx.paging.PagingState
import com.te6lim.ytcviewer.domain.DomainCard
import com.te6lim.ytcviewer.network.Response
import com.te6lim.ytcviewer.network.toDomainCards
import com.te6lim.ytcviewer.repository.CardRepository
import com.te6lim.ytcviewer.repository.CardRepository.Companion.PAGE_SIZE
import com.te6lim.ytcviewer.repository.CardType
import retrofit2.HttpException

private const val START_OFFSET = 0

class CardPagingSource(private val callback: CardRepository.Callback) :
    PagingSource<Int, DomainCard>() {

    override fun getRefreshKey(state: PagingState<Int, DomainCard>): Int? {
        return state.anchorPosition?.let {
            state.closestPageToPosition(it)?.prevKey?.plus(PAGE_SIZE)
                ?: state.closestPageToPosition(it)?.nextKey?.minus(PAGE_SIZE)
        }
    }

    override suspend fun load(params: LoadParams<Int>): LoadResult<Int, DomainCard> {
        val offset = params.key ?: START_OFFSET
        return try {

            val response = callback.getNetworkCards(offset)

            if (callback.getCardListType() == CardType.MONSTER)
                response as Response.MonsterCardResponse
            else response as Response.NonMonsterCardResponse

            val cards = response.data.toDomainCards()

            val nextKey = if (cards.isEmpty()) null else params.loadSize
            val prevKey = if (offset == START_OFFSET) null else offset - PAGE_SIZE

            LoadResult.Page(cards, prevKey = prevKey, nextKey = nextKey)

        } catch (e: Exception) {
            LoadResult.Error(e)
        } catch (e: HttpException) {
            LoadResult.Error(e)
        }
    }
}