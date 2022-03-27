package com.te6lim.ytcviewer.home.cards

import androidx.paging.*
import com.te6lim.ytcviewer.database.CardDatabase
import com.te6lim.ytcviewer.database.DatabaseMonsterCard
import com.te6lim.ytcviewer.database.RemoteKey
import com.te6lim.ytcviewer.network.NetworkCard
import com.te6lim.ytcviewer.network.toDatabaseMonsterCard
import com.te6lim.ytcviewer.network.toDatabaseNonMonsterCards
import com.te6lim.ytcviewer.repository.CardRepository
import com.te6lim.ytcviewer.repository.CardRepository.Companion.PAGE_SIZE
import com.te6lim.ytcviewer.repository.CardType
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

@OptIn(ExperimentalPagingApi::class)
class CardsSourceMediator(
    private val db: CardDatabase,
    private val callback: CardRepository.Callback
) : RemoteMediator<Int, DatabaseMonsterCard>() {

    override suspend fun initialize(): InitializeAction = InitializeAction.LAUNCH_INITIAL_REFRESH

    override suspend fun load(
        loadType: LoadType, state: PagingState<Int, DatabaseMonsterCard>
    ): MediatorResult {
        return getNetworkAndDatabaseMediatorResult(loadType, state)
    }

    private suspend fun getNetworkAndDatabaseMediatorResult(
        loadType: LoadType, state: PagingState<Int, DatabaseMonsterCard>
    ): MediatorResult {
        return try {
            getKey(loadType, state)?.let { key ->
                val cards = callback.getNetworkCards(key).data
                val isListEmpty = cards.isEmpty()

                if (loadType == LoadType.REFRESH)
                    clearCardsAndTheirRemoteKeys()

                val cardIds = insertCardsToDatabase(cards)

                val remoteKeys = generateRemoteKeysFromCardIds(key, cardIds)

                insertRemoteKeysToDatabase(remoteKeys)


                MediatorResult.Success(endOfPaginationReached = isListEmpty)
            } ?: MediatorResult.Success(endOfPaginationReached = false)

        } catch (e: Exception) {
            MediatorResult.Error(e)
        }
    }

    private suspend fun clearCardsAndTheirRemoteKeys() {
        db.monsterDao.clear()
        db.remoteKeysDao.clear()
    }

    private suspend fun insertCardsToDatabase(cards: List<NetworkCard>): List<Long> {
        return withContext(Dispatchers.IO) {
            val ids = mutableListOf<Long>()
            if (callback.getCardListType() == CardType.MONSTER) {
                for (card in cards) ids.add(db.monsterDao.insert(card.toDatabaseMonsterCard()))
            } else db.nonMonsterDao.insertMany(cards.toDatabaseNonMonsterCards())
            ids
        }
    }

    private suspend fun insertRemoteKeysToDatabase(remoteKeys: List<RemoteKey>) {
        withContext(Dispatchers.IO) {
            db.remoteKeysDao.insertMany(remoteKeys)
        }
    }

    private fun generateRemoteKeysFromCardIds(
        key: Int, cardIds: List<Long>
    ): List<RemoteKey> {
        val prevKey = if (key <= 0) null else key - PAGE_SIZE
        val nextKey = if (cardIds.isEmpty()) null else key + PAGE_SIZE

        return cardIds.map {
            RemoteKey(id = it, nextKey = nextKey, prevKey = prevKey)
        }
    }

    private suspend fun getKey(
        loadType: LoadType, state: PagingState<Int, DatabaseMonsterCard>
    ): Int? {
        return when (loadType) {
            LoadType.REFRESH -> {
                state.getKeyByAnchorPosition()
            }

            LoadType.APPEND -> {
                state.getNonEmptyLastPage()?.getNextKey()
            }

            LoadType.PREPEND -> {
                state.getNonEmptyFirstPage()?.getPreviousKey()
            }
        }
    }

    private fun PagingState<Int, DatabaseMonsterCard>.getNonEmptyLastPage() =
        pages.lastOrNull { it.data.isNotEmpty() }

    private fun PagingState<Int, DatabaseMonsterCard>.getNonEmptyFirstPage() =
        pages.firstOrNull { it.data.isNotEmpty() }

    private suspend fun PagingState<Int, DatabaseMonsterCard>.getKeyByAnchorPosition() =
        anchorPosition?.let { position ->
            closestItemToPosition(position)?.let { card ->
                db.remoteKeysDao.get(card.id)
            }?.let { remoteKey ->
                remoteKey.nextKey?.minus(PAGE_SIZE) ?: remoteKey.prevKey?.plus(PAGE_SIZE)
            } ?: 0
        } ?: 0

    private suspend fun PagingSource.LoadResult.Page<Int, DatabaseMonsterCard>.getNextKey() =
        data.lastOrNull()?.let { card -> db.remoteKeysDao.get(card.id)?.nextKey }

    private suspend fun PagingSource.LoadResult.Page<Int, DatabaseMonsterCard>.getPreviousKey() =
        data.firstOrNull()?.let { card -> db.remoteKeysDao.get(card.id)?.prevKey }
}