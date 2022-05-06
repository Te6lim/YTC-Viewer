package com.te6lim.ytcviewer.home.cards

import android.nfc.tech.MifareUltralight.PAGE_SIZE
import androidx.paging.LoadType
import androidx.paging.PagingState
import androidx.paging.RemoteMediator
import com.te6lim.ytcviewer.database.CardDatabase
import com.te6lim.ytcviewer.domain.DomainCard

@OptIn(androidx.paging.ExperimentalPagingApi::class)
class CardRemoteMediator(private val db: CardDatabase) : RemoteMediator<Int, DomainCard>() {
    override suspend fun load(loadType: LoadType, state: PagingState<Int, DomainCard>): MediatorResult {

        var key: Int? = null

        when (loadType) {
            LoadType.REFRESH -> {
                key = state.getKeyByAnchorPosition()
            }

            LoadType.APPEND -> {
                key = state.getNextKey()
            }

            LoadType.PREPEND -> {
                key = state.getPrevKey()
            }
        }
        TODO("Not yet implemented")
    }

    private suspend fun PagingState<Int, DomainCard>.getKeyByAnchorPosition(): Int {
        return anchorPosition?.let { position ->
            closestItemToPosition(position)?.let { card ->
                db.remoteKeysDao.get(card.id)
            }?.let { remoteKey ->
                remoteKey.nextKey?.minus(PAGE_SIZE) ?: remoteKey.prevKey?.plus(PAGE_SIZE)
            } ?: 0
        } ?: 0
    }

    private suspend fun PagingState<Int, DomainCard>.getNextKey(): Int? {
        return pages.lastOrNull()?.let { page ->
            page.data.lastOrNull()?.let { card ->
                db.remoteKeysDao.get(card.id)?.nextKey
            }
        }
    }

    private suspend fun PagingState<Int, DomainCard>.getPrevKey(): Int? {
        return pages.firstOrNull()?.let { page ->
            page.data.firstOrNull()?.let { card ->
                db.remoteKeysDao.get(card.id)?.prevKey
            }
        }
    }
}