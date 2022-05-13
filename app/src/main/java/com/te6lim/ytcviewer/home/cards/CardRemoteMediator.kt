package com.te6lim.ytcviewer.home.cards

import androidx.paging.LoadType
import androidx.paging.PagingState
import androidx.paging.RemoteMediator
import com.te6lim.ytcviewer.database.CardDatabase
import com.te6lim.ytcviewer.database.DatabaseCard
import com.te6lim.ytcviewer.database.RemoteKey
import com.te6lim.ytcviewer.network.PAGE_SIZE
import com.te6lim.ytcviewer.network.Response
import com.te6lim.ytcviewer.network.toDatabaseCard
import retrofit2.HttpException

@OptIn(androidx.paging.ExperimentalPagingApi::class)
class CardRemoteMediator(
    private val db: CardDatabase, val callback: Callback
) : RemoteMediator<Int, DatabaseCard>() {

    override suspend fun initialize(): InitializeAction = InitializeAction.LAUNCH_INITIAL_REFRESH

    override suspend fun load(loadType: LoadType, state: PagingState<Int, DatabaseCard>): MediatorResult {
        return getKeyByLoadType(loadType, state)?.let { newOffset ->
            getCardsOverHttpAndPerformCache(newOffset, loadType)
        } ?: MediatorResult.Success(false)
    }

    private suspend fun getCardsOverHttpAndPerformCache(newOffset: Int, loadType: LoadType): MediatorResult {
        return try {
            if (loadType != LoadType.PREPEND) {
                val response = callback.getNetworkCardsAsync(newOffset)
                insertCardsAndRemoteKeysToDb(loadType, response, newOffset)
            } else {
                MediatorResult.Success(false)
            }
        } catch (e: HttpException) {
            MediatorResult.Error(e)
        }
    }

    private suspend fun insertCardsAndRemoteKeysToDb(
        loadType: LoadType, response: Response, newOffset: Int
    ): MediatorResult.Success {
        val nextKey = if (response.meta.pagesRemaining == 0) null else response.meta.nextPageOffset
        val prevKey = if (response.meta.pagesRemaining == response.meta.totalPages) null
        else newOffset - PAGE_SIZE
        if (loadType == LoadType.REFRESH) clearDb()
        val cardIds = db.cardDao.insertMany(response.data.toDatabaseCard())
        db.remoteKeysDao.insertMany(cardIds.map { cardId ->
            RemoteKey(cardId, nextKey, prevKey)
        })
        return MediatorResult.Success(response.data.isEmpty())
    }

    private suspend fun clearDb() {
        db.cardDao.clear()
        db.remoteKeysDao.clear()
    }

    private suspend fun getKeyByLoadType(loadType: LoadType, state: PagingState<Int, DatabaseCard>): Int? =
        when (loadType) {
            LoadType.REFRESH -> {
                state.getKeyByAnchorPosition()
            }

            LoadType.APPEND -> {
                state.getNextKey()
            }

            LoadType.PREPEND -> {
                state.getPrevKey()
            }
        }

    private suspend fun PagingState<Int, DatabaseCard>.getKeyByAnchorPosition(): Int {
        return anchorPosition?.let { position ->
            closestItemToPosition(position)?.let { card ->
                db.remoteKeysDao.get(card.id)
            }?.let { remoteKey ->
                remoteKey.nextKey?.minus(PAGE_SIZE) ?: remoteKey.prevKey?.plus(PAGE_SIZE)
            } ?: 0
        } ?: 0
    }

    private suspend fun PagingState<Int, DatabaseCard>.getNextKey(): Int? {
        return pages.lastOrNull { page -> page.data.isNotEmpty() }?.data?.lastOrNull()?.let { card ->
            db.remoteKeysDao.get(card.id)?.nextKey
        }
    }

    private suspend fun PagingState<Int, DatabaseCard>.getPrevKey(): Int? {
        return pages.firstOrNull { page -> page.data.isNotEmpty() }?.data?.lastOrNull()?.let { card ->
            db.remoteKeysDao.get(card.id)?.prevKey
        }
    }

    interface Callback {
        suspend fun getNetworkCardsAsync(offset: Int): Response
    }
}