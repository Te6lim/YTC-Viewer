package com.te6lim.ytcviewer.home.cards

import android.nfc.tech.MifareUltralight.PAGE_SIZE
import androidx.paging.LoadType
import androidx.paging.PagingState
import androidx.paging.RemoteMediator
import com.te6lim.ytcviewer.database.CardDatabase
import com.te6lim.ytcviewer.database.DatabaseCard
import com.te6lim.ytcviewer.network.Response
import kotlinx.coroutines.Deferred

@OptIn(androidx.paging.ExperimentalPagingApi::class)
class CardRemoteMediator(
    private val db: CardDatabase, val callback: Callback
) : RemoteMediator<Int, DatabaseCard>() {
    override suspend fun load(loadType: LoadType, state: PagingState<Int, DatabaseCard>): MediatorResult {

        val key = getKeyByLoadType(loadType, state)
        key?.let { offset ->
            callback.getNetworkCardsAsync(offset)
        }
        TODO("Not yet implemented")
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
        fun getNetworkCardsAsync(offset: Int): Deferred<Response>
    }
}