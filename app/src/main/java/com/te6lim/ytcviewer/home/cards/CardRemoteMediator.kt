package com.te6lim.ytcviewer.home.cards

import androidx.paging.LoadType
import androidx.paging.PagingState
import androidx.paging.RemoteMediator
import com.te6lim.ytcviewer.database.CardDatabase
import com.te6lim.ytcviewer.database.DatabaseCard
import com.te6lim.ytcviewer.database.RemoteKey
import com.te6lim.ytcviewer.network.NetworkCard
import com.te6lim.ytcviewer.network.PAGE_SIZE
import com.te6lim.ytcviewer.network.Response
import com.te6lim.ytcviewer.network.toDatabaseCard
import retrofit2.HttpException

@OptIn(androidx.paging.ExperimentalPagingApi::class)
class CardRemoteMediator(
    private val db: CardDatabase, val callback: Callback
) : RemoteMediator<Int, DatabaseCard>() {
    override suspend fun load(loadType: LoadType, state: PagingState<Int, DatabaseCard>): MediatorResult {

        return getKeyByLoadType(loadType, state)?.let { newOffset ->
            val cards: List<NetworkCard>

            try {
                cards = callback.getNetworkCardsAsync(newOffset).data
                if (loadType == LoadType.REFRESH) {
                    db.cardDao.clear()
                    db.remoteKeysDao.clear()
                }
                val cardIds = db.cardDao.insertMany(cards.toDatabaseCard())
                db.remoteKeysDao.insertMany(cardIds.map { cardId ->
                    RemoteKey(cardId, newOffset + PAGE_SIZE, newOffset - PAGE_SIZE)
                })
                MediatorResult.Success(cards.isEmpty())
            } catch (e: HttpException) {
                MediatorResult.Error(e)
            }

        } ?: MediatorResult.Success(false)
    }

    private suspend fun getKeyByLoadType(loadType: LoadType, state: PagingState<Int, DatabaseCard>) =
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
        return pages.lastOrNull()?.let { page ->
            page.data.lastOrNull()?.let { card ->
                db.remoteKeysDao.get(card.id)?.nextKey
            }
        }
    }

    private suspend fun PagingState<Int, DatabaseCard>.getPrevKey(): Int? {
        return pages.firstOrNull()?.let { page ->
            page.data.firstOrNull()?.let { card ->
                db.remoteKeysDao.get(card.id)?.prevKey
            }
        }
    }

    interface Callback {
        suspend fun getNetworkCardsAsync(offset: Int): Response
    }
}