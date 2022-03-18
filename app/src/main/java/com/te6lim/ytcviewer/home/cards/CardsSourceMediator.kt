package com.te6lim.ytcviewer.home.cards

import androidx.paging.ExperimentalPagingApi
import androidx.paging.LoadType
import androidx.paging.PagingState
import androidx.paging.RemoteMediator
import com.te6lim.ytcviewer.database.CardDatabase
import com.te6lim.ytcviewer.domain.DomainCard
import com.te6lim.ytcviewer.network.toDatabaseMonsterCards
import com.te6lim.ytcviewer.network.toDatabaseNonMonsterCards
import com.te6lim.ytcviewer.repository.Callback
import com.te6lim.ytcviewer.repository.CardType

@ExperimentalPagingApi
class CardsSourceMediator(private val cardDb: CardDatabase, private val callback: Callback) :
    RemoteMediator<Int, DomainCard>() {
    override suspend fun load(
        loadType: LoadType,
        state: PagingState<Int, DomainCard>
    ): MediatorResult {
        val offset = 0
        return try {
            val response = callback.getNetworkCards(offset)
            val cards = response.data
            if (callback.getCardListType() == CardType.MONSTER)
                cardDb.monsterDao.insertMany(*cards.toDatabaseMonsterCards().toTypedArray())
            else cardDb.nonMonsterDao.insertMany(*cards.toDatabaseNonMonsterCards().toTypedArray())
            MediatorResult.Success(
                endOfPaginationReached = cards.isEmpty()
            )
        } catch (e: Exception) {
            MediatorResult.Error(e)
        }
    }
}