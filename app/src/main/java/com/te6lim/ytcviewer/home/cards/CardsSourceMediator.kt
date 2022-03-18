package com.te6lim.ytcviewer.home.cards

import androidx.paging.ExperimentalPagingApi
import androidx.paging.LoadType
import androidx.paging.PagingState
import androidx.paging.RemoteMediator
import com.te6lim.ytcviewer.database.CardDatabase
import com.te6lim.ytcviewer.network.NetworkCard
import com.te6lim.ytcviewer.network.toDatabaseMonsterCards
import com.te6lim.ytcviewer.network.toDatabaseNonMonsterCards
import com.te6lim.ytcviewer.repository.Callback
import com.te6lim.ytcviewer.repository.CardType

@ExperimentalPagingApi
class CardsSourceMediator(private val cardDb: CardDatabase, private val callback: Callback) :
    RemoteMediator<Int, NetworkCard>() {
    override suspend fun load(
        loadType: LoadType,
        state: PagingState<Int, NetworkCard>
    ): MediatorResult {
        val offset = 0
        try {
            val response = callback.getCards(offset)
            val cards = response.data
            if (callback.getCardListType() == CardType.MONSTER)
                cardDb.monsterDao.insertMany(*cards.toDatabaseMonsterCards().toTypedArray())
            else cardDb.nonMonsterDao.insertMany(*cards.toDatabaseNonMonsterCards().toTypedArray())
            return MediatorResult.Success(
                endOfPaginationReached = cards.isEmpty()
            )
        } catch (e: Exception) {
            return MediatorResult.Error(e)
        }
    }
}