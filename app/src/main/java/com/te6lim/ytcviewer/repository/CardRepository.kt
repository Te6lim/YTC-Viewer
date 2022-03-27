package com.te6lim.ytcviewer.repository

import androidx.paging.ExperimentalPagingApi
import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import com.te6lim.ytcviewer.database.CardDatabase
import com.te6lim.ytcviewer.database.DatabaseCard
import com.te6lim.ytcviewer.filters.CardFilterCategory
import com.te6lim.ytcviewer.home.cards.CardsSourceMediator
import com.te6lim.ytcviewer.network.Response
import com.te6lim.ytcviewer.network.YtcApi
import kotlinx.coroutines.Deferred
import kotlinx.coroutines.flow.Flow

enum class CardType {
    GENERAL, MONSTER, NON_MONSTER
}

class CardRepository(private val db: CardDatabase) {

    companion object {
        const val PAGE_SIZE = 10
    }

    var cardListType: CardType = CardType.GENERAL
        private set

    private fun Array<String>.convertParameterListToFormattedString(): String {
        val arguments = StringBuilder()
        for ((i, string) in withIndex()) {
            arguments.append(string)
            if (i != size - 1) arguments.append(",")
        }
        return arguments.toString()
    }

    @OptIn(ExperimentalPagingApi::class)
    private suspend fun getCards(
        selectedFilters: Map<String, Array<String>>, lastChecked: String, offset: Int = 0
    ): Response {
        var cardsDeferred: Deferred<Response>? = null

        val keys = selectedFilters.keys.toList()

        when (selectedFilters.size) {
            1 -> {
                if (lastChecked == CardFilterCategory.Spell.name) {
                    cardListType = CardType.NON_MONSTER
                    cardsDeferred = YtcApi.retrofitService.getNonMonsterCardsAsync(
                        mapOf(Pair("type", "spell card")),
                        mapOf(
                            Pair(
                                keys[0],
                                selectedFilters[keys[0]]!!
                                    .convertParameterListToFormattedString()
                            )
                        ),
                        offset = offset
                    )
                } else {
                    if (lastChecked == CardFilterCategory.Trap.name) {
                        cardListType = CardType.NON_MONSTER
                        cardsDeferred = YtcApi.retrofitService.getNonMonsterCardsAsync(
                            mapOf(Pair("type", "trap card")),
                            mapOf(
                                Pair(
                                    keys[0], selectedFilters[keys[0]]!!
                                        .convertParameterListToFormattedString()
                                )
                            ),
                            offset = offset
                        )
                    } else {
                        cardListType = CardType.MONSTER
                        cardsDeferred = YtcApi.retrofitService.getMonsterCardsAsync(
                            mapOf(
                                Pair(
                                    keys[0], selectedFilters[keys[0]]!!
                                        .convertParameterListToFormattedString()
                                )
                            ),
                            offset = offset
                        )
                    }
                }
            }

            2 -> {
                cardListType = CardType.MONSTER
                cardsDeferred = YtcApi.retrofitService.getMonsterCardsAsync(
                    mapOf(
                        Pair(
                            keys[0],
                            selectedFilters[keys[0]]!!
                                .convertParameterListToFormattedString()
                        )
                    ),
                    mapOf(
                        Pair(
                            keys[1], selectedFilters[keys[1]]!!
                                .convertParameterListToFormattedString()
                        )
                    ),
                    offset = offset
                )
            }

            3 -> {
                cardListType = CardType.MONSTER
                cardsDeferred = YtcApi.retrofitService.getMonsterCardsAsync(
                    mapOf(
                        Pair(
                            keys[0], selectedFilters[keys[0]]!!
                                .convertParameterListToFormattedString()
                        )
                    ),
                    mapOf(
                        Pair(
                            keys[1], selectedFilters[keys[1]]!!
                                .convertParameterListToFormattedString()
                        )
                    ),
                    mapOf(
                        Pair(
                            keys[2], selectedFilters[keys[2]]!!
                                .convertParameterListToFormattedString()
                        )
                    ),
                    offset = offset
                )
            }
        }
        return cardsDeferred!!.await()
    }

    @OptIn(ExperimentalPagingApi::class)
    fun getCardStream(selectedFilters: Map<String, Array<String>>, lastChecked: String)
            : Flow<PagingData<DatabaseCard>> {
        val databasePagingSource = { db.cardDao.getSource() }

        return Pager(
            config = PagingConfig(pageSize = PAGE_SIZE, enablePlaceholders = false),
            remoteMediator = CardsSourceMediator(db, object : Callback {

                override suspend fun getNetworkCards(offset: Int) =
                    getCards(selectedFilters, lastChecked, offset)

            }), pagingSourceFactory = databasePagingSource
        ).flow
    }

    interface Callback {

        suspend fun getNetworkCards(offset: Int): Response {
            return Response(listOf())
        }
    }
}