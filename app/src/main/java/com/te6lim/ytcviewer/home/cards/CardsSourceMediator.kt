package com.te6lim.ytcviewer.home.cards

import androidx.paging.*
import com.te6lim.ytcviewer.database.CardDatabase
import com.te6lim.ytcviewer.database.DatabaseMonsterCard
import com.te6lim.ytcviewer.database.RemoteKey
import com.te6lim.ytcviewer.network.NetworkCard
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
                val cards = callback.getNetworkCards(key).data

                if (loadType == LoadType.REFRESH) {
                    clearCardsAndTheirRemoteKeys()
                }

                val keys = generateListOfRemoteKeysFromCardList(key, cards)

                insertCardsAndTheirRemoteKeysToDatabase(cards, keys)

                MediatorResult.Success(endOfPaginationReached = cards.isEmpty())

            } catch (e: Exception) {
                MediatorResult.Error(e)
            } catch (e: HttpException) {
                MediatorResult.Error(e)
            }

        } ?: return MediatorResult.Success(endOfPaginationReached = false)
    }

    private suspend fun clearCardsAndTheirRemoteKeys() {
        db.monsterDao.clear()
        db.remoteKeysDao.clear()
    }

    private suspend fun insertCardsAndTheirRemoteKeysToDatabase(
        cards: List<NetworkCard>, keys: List<RemoteKey>
    ) {
        withContext(Dispatchers.IO) {
            if (callback.getCardListType() == CardType.MONSTER) {
                db.monsterDao.insertMany(*cards.toDatabaseMonsterCards().toTypedArray())
                db.remoteKeysDao.insertMany(keys)
            } else db.nonMonsterDao.insertMany(
                *cards.toDatabaseNonMonsterCards().toTypedArray()
            )
        }
    }

    private fun generateListOfRemoteKeysFromCardList(
        key: Int, cards: List<NetworkCard>
    ): List<RemoteKey> {
        val prevKey = if (key == 0) null else key - 1
        val nextKey = if (cards.isEmpty()) null else key + 1

        val keys = cards.map {
            RemoteKey(id = it.id, nextKey = nextKey, prevKey = prevKey)
        }
        return keys
    }

    private suspend fun getKey(loadType: LoadType, state: PagingState<Int, DatabaseMonsterCard>)
            : Int? {
        return when (loadType) {
            LoadType.REFRESH -> {
                state.getKeyByAnchorPosition()
            }

            LoadType.APPEND -> {
                return state.pages.lastOrNull { it.data.isNotEmpty() }?.getNextKey()
            }

            LoadType.PREPEND -> {
                return state.pages.firstOrNull { it.data.isNotEmpty() }?.getPreviousKey()
            }
        }
    }

    private suspend fun PagingState<Int, DatabaseMonsterCard>.getKeyByAnchorPosition(): Int? {
        return anchorPosition?.let { position ->
            closestItemToPosition(position)?.let { card ->
                db.remoteKeysDao.get(card.id)
            }?.let { remoteKey ->
                remoteKey.nextKey?.minus(1) ?: remoteKey.prevKey?.plus(1)
            } ?: 0
        }
    }

    private suspend fun PagingSource.LoadResult.Page<Int, DatabaseMonsterCard>.getNextKey(): Int? {
        return this.data.lastOrNull()
            ?.let { card ->
                db.remoteKeysDao.get(card.id)?.nextKey
            }
    }

    private suspend fun PagingSource.LoadResult.Page<Int, DatabaseMonsterCard>.getPreviousKey()
            : Int? {
        return data.firstOrNull()
            ?.let { card ->
                db.remoteKeysDao.get(card.id)?.prevKey
            }
    }
}