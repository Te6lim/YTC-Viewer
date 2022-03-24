package com.te6lim.ytcviewer.repository

import androidx.lifecycle.*
import androidx.paging.*
import com.te6lim.ytcviewer.database.*
import com.te6lim.ytcviewer.filters.CardFilterCategory
import com.te6lim.ytcviewer.home.cards.CardsSourceMediator
import com.te6lim.ytcviewer.network.*
import kotlinx.coroutines.*
import kotlinx.coroutines.flow.Flow
import java.util.*

enum class CardType {
    MONSTER, NON_MONSTER
}

class CardRepository(
    private val db: CardDatabase, private val lastTypeCached: MutableLiveData<CardType?>
) {

    companion object {
        const val PAGE_SIZE = 100
    }

    private fun Array<String>.formattedString(): String {
        val arguments = StringBuilder().apply {
            for ((i, string) in this@formattedString.withIndex()) {
                append(string)
                if (i != size - 1) append(",")
            }
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
                    lastTypeCached.value = CardType.NON_MONSTER
                    cardsDeferred = YtcApi.retrofitService.getNonMonsterCardsAsync(
                        mapOf(Pair("type", "spell card")),
                        mapOf(Pair(keys[0], selectedFilters[keys[0]]!!.formattedString())),
                        offset = offset
                    )
                } else {
                    if (lastChecked == CardFilterCategory.Trap.name) {
                        lastTypeCached.value = CardType.NON_MONSTER
                        cardsDeferred = YtcApi.retrofitService.getNonMonsterCardsAsync(
                            mapOf(Pair("type", "trap card")),
                            mapOf(Pair(keys[0], selectedFilters[keys[0]]!!.formattedString())),
                            offset = offset
                        )
                    } else {
                        lastTypeCached.value = CardType.MONSTER
                        cardsDeferred = YtcApi.retrofitService.getMonsterCardsAsync(
                            mapOf(Pair(keys[0], selectedFilters[keys[0]]!!.formattedString())),
                            offset = offset
                        )
                    }
                }
            }

            2 -> {
                lastTypeCached.value = CardType.MONSTER
                cardsDeferred = YtcApi.retrofitService.getMonsterCardsAsync(
                    mapOf(Pair(keys[0], selectedFilters[keys[0]]!!.formattedString())),
                    mapOf(Pair(keys[1], selectedFilters[keys[1]]!!.formattedString())),
                    offset = offset
                )
            }

            3 -> {
                lastTypeCached.value = CardType.MONSTER
                cardsDeferred = YtcApi.retrofitService.getMonsterCardsAsync(
                    mapOf(Pair(keys[0], selectedFilters[keys[0]]!!.formattedString())),
                    mapOf(Pair(keys[1], selectedFilters[keys[1]]!!.formattedString())),
                    mapOf(Pair(keys[2], selectedFilters[keys[2]]!!.formattedString())),
                    offset = offset
                )
            }
        }
        return cardsDeferred!!.await()
    }

    @OptIn(ExperimentalPagingApi::class)
    fun getCardStream(selectedFilters: Map<String, Array<String>>, lastChecked: String)
            : Flow<PagingData<DatabaseMonsterCard>> {
        return Pager(
            config = PagingConfig(pageSize = PAGE_SIZE, enablePlaceholders = false),
            remoteMediator = CardsSourceMediator(db, object : Callback {
                override suspend fun getNetworkCards(offset: Int): Response {
                    return getCards(selectedFilters, lastChecked, offset)
                }

                override fun getCardListType() = lastTypeCached.value
            })
        ) {
            db.monsterDao.getSource()
        }.flow
    }

    interface Callback {

        suspend fun getNetworkCards(offset: Int): Response {
            return Response(listOf())
        }

        fun getCardListType(): CardType?
    }
}