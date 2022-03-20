package com.te6lim.ytcviewer.home.cards

import androidx.paging.ExperimentalPagingApi
import androidx.paging.LoadType
import androidx.paging.PagingState
import androidx.paging.RemoteMediator
import com.te6lim.ytcviewer.database.CardDatabase
import com.te6lim.ytcviewer.domain.DomainCard
import com.te6lim.ytcviewer.network.toDatabaseMonsterCards
import com.te6lim.ytcviewer.network.toDatabaseNonMonsterCards
import com.te6lim.ytcviewer.repository.CardRepository
import com.te6lim.ytcviewer.repository.CardRepository.Companion.PAGE_SIZE
import com.te6lim.ytcviewer.repository.CardType
import retrofit2.HttpException

@OptIn(ExperimentalPagingApi::class)
class CardsSourceMediator(
    private val cardDb: CardDatabase,
    private val callback: CardRepository.Callback
) :
    RemoteMediator<Int, DomainCard>() {

    override suspend fun initialize(): InitializeAction = InitializeAction.LAUNCH_INITIAL_REFRESH

    override suspend fun load(
        loadType: LoadType, state: PagingState<Int, DomainCard>
    ): MediatorResult {

        val offset = getKey(loadType, state)
        return try {
            offset?.let {
                val response = callback.getNetworkCards(it)
                val cards = response.data

                if (callback.getCardListType() == CardType.MONSTER)
                    cardDb.monsterDao.insertMany(*cards.toDatabaseMonsterCards().toTypedArray())
                else cardDb.nonMonsterDao.insertMany(
                    *cards.toDatabaseNonMonsterCards().toTypedArray()
                )

                MediatorResult.Success(endOfPaginationReached = cards.isEmpty())
            } ?: MediatorResult.Success(endOfPaginationReached = false)

        } catch (e: Exception) {
            MediatorResult.Error(e)
        } catch (e: HttpException) {
            MediatorResult.Error(e)
        }
    }

    private fun getKey(loadType: LoadType, state: PagingState<Int, DomainCard>): Int? {
        when (loadType) {
            LoadType.REFRESH -> {
                with(state) {
                    return anchorPosition?.let {
                        closestPageToPosition(it)?.nextKey?.minus(PAGE_SIZE)
                            ?: closestPageToPosition(it)?.prevKey?.plus(PAGE_SIZE) ?: 0
                    }
                }
            }

            LoadType.APPEND -> {
                return state.pages.last().nextKey
            }

            LoadType.PREPEND -> {
                with(state) {
                    return anchorPosition?.let {
                        closestPageToPosition(it)?.prevKey
                    }
                }
            }
        }
    }
}