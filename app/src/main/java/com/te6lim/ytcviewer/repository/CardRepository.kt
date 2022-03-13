package com.te6lim.ytcviewer.repository

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Transformations
import com.te6lim.ytcviewer.database.CardDatabase
import com.te6lim.ytcviewer.database.toDomainMonsterCards
import com.te6lim.ytcviewer.database.toDomainNonMonsterCards
import com.te6lim.ytcviewer.domain.DomainCard
import com.te6lim.ytcviewer.filters.FilterSelectionViewModel
import com.te6lim.ytcviewer.network.*
import kotlinx.coroutines.*

class CardRepository(
    private val cardDb: CardDatabase, private val networkStatus: MutableLiveData<NetworkStatus>
) {

    private enum class CardType {
        MONSTER, NON_MONSTER
    }

    private val job = Job()
    private val scope = CoroutineScope(job + Dispatchers.Main)

    val monsterCards: LiveData<List<DomainCard>> = Transformations.map(
        cardDb.monsterDao.getAll()
    ) {
        it.toDomainMonsterCards()
    }

    val nonMonsterCards: LiveData<List<DomainCard>> = Transformations.map(
        cardDb.nonMonsterDao.getAll()
    ) {
        it.toDomainNonMonsterCards()
    }

    private val _cardMix = MutableLiveData<List<DomainCard>>()
    val cardMix: LiveData<List<DomainCard>> get() = _cardMix

    var lastSearchQuery: String? = null
        private set

    private var type: CardType? = null

    private fun Array<String>.formattedString(): String {
        val arguments = StringBuilder().apply {
            for ((i, string) in this@formattedString.withIndex()) {
                append(string)
                if (i != size - 1) append(",")
            }
        }
        return arguments.toString()
    }

    fun getCards(selectedFilters: Map<String, Array<String>>, lastChecked: String) {
        lastSearchQuery = null
        scope.launch {
            withContext(Dispatchers.IO) {
                var cardsDeferred: Deferred<Response>? = null

                val keys = selectedFilters.keys.toList()

                when (selectedFilters.size) {
                    1 -> {
                        if (lastChecked == FilterSelectionViewModel.CardFilterCategory.Spell.name) {
                            type = CardType.NON_MONSTER
                            cardsDeferred = YtcApi.retrofitService.getNonMonsterCardsAsync(
                                mapOf(Pair("type", "spell card")),
                                mapOf(
                                    Pair(
                                        keys[0],
                                        selectedFilters[keys[0]]!!.formattedString()
                                    )
                                )
                            )
                        } else {
                            cardsDeferred =
                                if (lastChecked == FilterSelectionViewModel
                                        .CardFilterCategory.Trap.name
                                ) {
                                    type = CardType.NON_MONSTER
                                    YtcApi.retrofitService.getNonMonsterCardsAsync(
                                        mapOf(Pair("type", "trap card")),
                                        mapOf(
                                            Pair(
                                                keys[0],
                                                selectedFilters[keys[0]]!!.formattedString()
                                            )
                                        )
                                    )
                                } else {
                                    type = CardType.MONSTER
                                    YtcApi.retrofitService.getCardsAsync(
                                        mapOf(
                                            Pair(
                                                keys[0],
                                                selectedFilters[keys[0]]!!.formattedString()
                                            )
                                        )
                                    )
                                }
                        }
                    }

                    2 -> {
                        type = CardType.MONSTER
                        cardsDeferred = YtcApi.retrofitService.getCardsAsync(
                            mapOf(Pair(keys[0], selectedFilters[keys[0]]!!.formattedString())),
                            mapOf(Pair(keys[1], selectedFilters[keys[1]]!!.formattedString()))
                        )
                    }

                    3 -> {
                        type = CardType.MONSTER
                        cardsDeferred = YtcApi.retrofitService.getCardsAsync(
                            mapOf(Pair(keys[0], selectedFilters[keys[0]]!!.formattedString())),
                            mapOf(Pair(keys[1], selectedFilters[keys[1]]!!.formattedString())),
                            mapOf(Pair(keys[2], selectedFilters[keys[2]]!!.formattedString()))
                        )
                    }
                }

                try {
                    networkStatus.value = NetworkStatus.LOADING
                    when (type) {
                        CardType.MONSTER -> {
                            cardDb.monsterDao.insertMany(
                                *(cardsDeferred!!.await().data)
                                    .toDatabaseMonsterCards().toTypedArray()
                            )
                        }

                        CardType.NON_MONSTER -> {
                            cardDb.nonMonsterDao.insertMany(
                                *(cardsDeferred!!.await().data)
                                    .toDatabaseNonMonsterCards().toTypedArray()
                            )
                        }

                        else -> {
                        }
                    }
                    networkStatus.value = NetworkStatus.DONE
                } catch (e: Exception) {
                    networkStatus.value = NetworkStatus.ERROR
                }
            }
        }
    }

    fun getCardsWithSearch(key: String) {
        lastSearchQuery = key
        scope.launch {

            val cardsDeferred: Deferred<Response> =
                YtcApi.retrofitService.getCardsBySearchAsync(key)

            try {
                networkStatus.value = NetworkStatus.LOADING
                _cardMix.value = cardsDeferred.await().data.toDomainCards()
                networkStatus.value = NetworkStatus.DONE
            } catch (e: Exception) {
                networkStatus.value = NetworkStatus.ERROR
            }

        }
    }

}