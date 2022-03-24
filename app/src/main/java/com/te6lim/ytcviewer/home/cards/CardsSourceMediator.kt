package com.te6lim.ytcviewer.home.cards

import androidx.paging.ExperimentalPagingApi
import androidx.paging.LoadType
import androidx.paging.PagingState
import androidx.paging.RemoteMediator
import com.te6lim.ytcviewer.database.CardDatabase
import com.te6lim.ytcviewer.database.DatabaseMonsterCard
import com.te6lim.ytcviewer.database.RemoteKey
import com.te6lim.ytcviewer.network.toDatabaseMonsterCards
import com.te6lim.ytcviewer.network.toDatabaseNonMonsterCards
import com.te6lim.ytcviewer.repository.CardRepository
import com.te6lim.ytcviewer.repository.CardType
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import retrofit2.HttpException

@OptIn(ExperimentalPagingApi::class)
class CardsSourceMediator(
    private val db: CardDatabase,
    private val callback: CardRepository.Callback
) : RemoteMediator<Int, DatabaseMonsterCard>() {

    private var key: Int? = 0

    override suspend fun initialize(): InitializeAction = InitializeAction.LAUNCH_INITIAL_REFRESH

    override suspend fun load(
        loadType: LoadType, state: PagingState<Int, DatabaseMonsterCard>
    ): MediatorResult {

        key = getKey(loadType, state)

        key?.let { key ->

            return try {
                val response = callback.getNetworkCards(key)
                val cards = response.data

                if (loadType == LoadType.REFRESH) {
                    db.monsterDao.clear()
                    db.remoteKeysDao.clear()
                }

                val prevKey = if (key == 0) null else key - 1
                val nextKey = if (cards.isEmpty()) null else key + 1

                val keys = cards.map {
                    RemoteKey(id = it.id, nextKey = nextKey, prevKey = prevKey)
                }

                withContext(Dispatchers.IO) {
                    if (callback.getCardListType() == CardType.MONSTER) {
                        db.monsterDao.insertMany(*cards.toDatabaseMonsterCards().toTypedArray())
                        db.remoteKeysDao.insertMany(keys)
                    } else db.nonMonsterDao.insertMany(
                        *cards.toDatabaseNonMonsterCards().toTypedArray()
                    )
                }

                MediatorResult.Success(endOfPaginationReached = cards.isEmpty())

            } catch (e: Exception) {
                MediatorResult.Error(e)
            } catch (e: HttpException) {
                MediatorResult.Error(e)
            }

        } ?: return MediatorResult.Success(endOfPaginationReached = false)
    }

    private suspend fun getKey(loadType: LoadType, state: PagingState<Int, DatabaseMonsterCard>)
            : Int? {
        when (loadType) {
            LoadType.REFRESH -> {
                return with(state) {
                    anchorPosition?.let { position ->
                        closestItemToPosition(position)?.let { card ->
                            db.remoteKeysDao.get(card.id)
                        }?.let { remoteKey ->
                            remoteKey.nextKey?.minus(1) ?: remoteKey.prevKey?.plus(1)
                        } ?: 0
                    }
                }
            }

            LoadType.APPEND -> {
                return state.pages.lastOrNull { it.data.isNotEmpty() }?.data?.lastOrNull()
                    ?.let { card ->
                        db.remoteKeysDao.get(card.id)?.nextKey
                    }
            }

            LoadType.PREPEND -> {
                return state.pages.firstOrNull { it.data.isNotEmpty() }?.data?.firstOrNull()
                    ?.let { card ->
                        db.remoteKeysDao.get(card.id)?.prevKey
                    }
            }
        }
    }
}