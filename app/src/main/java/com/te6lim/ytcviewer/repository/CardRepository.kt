package com.te6lim.ytcviewer.repository

import androidx.lifecycle.*
import androidx.paging.*
import com.te6lim.ytcviewer.database.*
import com.te6lim.ytcviewer.domain.DomainCard
import com.te6lim.ytcviewer.filters.CardFilterCategory
import com.te6lim.ytcviewer.home.cards.CardsSourceMediator
import com.te6lim.ytcviewer.network.*
import kotlinx.coroutines.*
import java.util.*

enum class CardType {
    MONSTER, NON_MONSTER
}

class CardRepository(
    private val cardDb: CardDatabase, private val networkStatus: MutableLiveData<NetworkStatus>
) {

    companion object {
        const val PAGE_SIZE = 100
    }

    private val job = Job()
    private val scope = CoroutineScope(job + Dispatchers.Main)

    /*private val monsterCards = Pager(PagingConfig(PAGE_SIZE, enablePlaceholders = false))
    {
        DatabasePagingSource(cardDb, object : Callback {

            override fun getCardListType() = CardType.MONSTER

            override suspend fun getDatabaseMonsterCardsInRange(top: Int, bottom: Int)
                    : List<DatabaseMonsterCard>? = cardDb.monsterDao.getAllInRange(top, bottom)

        })
    }.liveData

    private val nonMonsterCards = Pager(PagingConfig(PAGE_SIZE, enablePlaceholders = false))
    {
        DatabasePagingSource(cardDb, object : Callback {

            override fun getCardListType(): CardType = CardType.NON_MONSTER

            override suspend fun getDatabaseNonMonsterCardsInRange(top: Int, bottom: Int)
                    : List<DatabaseNonMonsterCard>? =
                cardDb.nonMonsterDao.getAllInRange(top, bottom)

        })
    }.liveData*/

    private val cardMix = MutableLiveData<List<DomainCard>>()

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

    /*fun resolveCardListSource(): LiveData<PagingData<DomainCard>> {
        val mediator = MediatorLiveData<PagingData<DomainCard>>()

        val monsterCardsObserver = Observer<PagingData<DomainCard>> {
            monsterCards.value?.let {
                type = CardType.MONSTER
                mediator.value = monsterCards.value
                scope.launch {
                    withContext(Dispatchers.IO) { cardDb.nonMonsterDao.clear() }
                }
            }
        }

        val nonMonsterCardsObserver = Observer<PagingData<DomainCard>> {
            nonMonsterCards.value?.let {
                type = CardType.NON_MONSTER
                mediator.value = nonMonsterCards.value
                scope.launch {
                    withContext(Dispatchers.IO) { cardDb.monsterDao.clear() }
                }
            }
        }

        *//*val cardMixObserver = Observer<PagingSource<Int, DomainCard>> {
            cardMix.value?.let {
                if (dummyList.isNullOrEmpty())
            }
        }*//*

        mediator.addSource(monsterCards, monsterCardsObserver)
        mediator.addSource(nonMonsterCards, nonMonsterCardsObserver)
        //mediator.addSource(cardMix, cardMixObserver)
        return mediator
    }*/

    @OptIn(ExperimentalPagingApi::class)
    suspend fun getCards(
        selectedFilters: Map<String, Array<String>>, lastChecked: String, offset: Int = 0
    ): Response {
        var cardsDeferred: Deferred<Response>? = null

        val keys = selectedFilters.keys.toList()

        when (selectedFilters.size) {
            1 -> {
                if (lastChecked == CardFilterCategory.Spell.name) {
                    type = CardType.NON_MONSTER
                    cardsDeferred = YtcApi.retrofitService.getNonMonsterCardsAsync(
                        mapOf(Pair("type", "spell card")),
                        mapOf(Pair(keys[0], selectedFilters[keys[0]]!!.formattedString())),
                        offset = offset
                    )
                } else {
                    if (lastChecked == CardFilterCategory.Trap.name) {
                        type = CardType.NON_MONSTER
                        cardsDeferred = YtcApi.retrofitService.getNonMonsterCardsAsync(
                            mapOf(Pair("type", "trap card")),
                            mapOf(Pair(keys[0], selectedFilters[keys[0]]!!.formattedString())),
                            offset = offset
                        )
                    } else {
                        type = CardType.MONSTER
                        cardsDeferred = YtcApi.retrofitService.getCardsAsync(
                            mapOf(Pair(keys[0], selectedFilters[keys[0]]!!.formattedString())),
                            offset = offset
                        )
                    }
                }
            }

            2 -> {
                type = CardType.MONSTER
                cardsDeferred = YtcApi.retrofitService.getCardsAsync(
                    mapOf(Pair(keys[0], selectedFilters[keys[0]]!!.formattedString())),
                    mapOf(Pair(keys[1], selectedFilters[keys[1]]!!.formattedString())),
                    offset = offset
                )
            }

            3 -> {
                type = CardType.MONSTER
                cardsDeferred = YtcApi.retrofitService.getCardsAsync(
                    mapOf(Pair(keys[0], selectedFilters[keys[0]]!!.formattedString())),
                    mapOf(Pair(keys[1], selectedFilters[keys[1]]!!.formattedString())),
                    mapOf(Pair(keys[2], selectedFilters[keys[2]]!!.formattedString())),
                    offset = offset
                )
            }
        }

        /*try {
            networkStatus.value = NetworkStatus.LOADING
            return cardsDeferred!!.await()
            *//*withContext(Dispatchers.IO) {
                when (type) {
                    CardType.MONSTER -> {
                        *//**//*val array = cardList.toDatabaseMonsterCards().toTypedArray()
                            cardDb.monsterDao.clear()
                            cardDb.monsterDao.insertMany(*array)
                            networkStatus.postValue(NetworkStatus.DONE)*//**//*
                        }

                        CardType.NON_MONSTER -> {
                            val array = cardList.toDatabaseNonMonsterCards().toTypedArray()
                            cardDb.nonMonsterDao.clear()
                            cardDb.nonMonsterDao.insertMany(*array)
                            networkStatus.postValue(NetworkStatus.DONE)
                        }

                        else -> {
                        }
                    }
                }*//*
        } catch (e: Exception) {
            //networkStatus.value = NetworkStatus.ERROR
        }*/
        return cardsDeferred!!.await()
    }

    fun getCardsWithSearch(key: String) {
        scope.launch {
            val cardsDeferred: Deferred<Response> =
                YtcApi.retrofitService.getCardsBySearchAsync(key)

            try {
                networkStatus.value = NetworkStatus.LOADING
                cardMix.value = cardsDeferred.await().data.toDomainCards()
                networkStatus.value = NetworkStatus.DONE
            } catch (e: Exception) {
                networkStatus.value = NetworkStatus.ERROR
            }
        }
    }

    @OptIn(ExperimentalPagingApi::class)
    fun getCardStream(selectedFilters: Map<String, Array<String>>, lastChecked: String)
            : LiveData<PagingData<DomainCard>> {
        return Pager(
            config = PagingConfig(pageSize = PAGE_SIZE, enablePlaceholders = false),
            remoteMediator = CardsSourceMediator(cardDb, object : Callback {
                override suspend fun getNetworkCards(offset: Int): Response {
                    return getCards(selectedFilters, lastChecked, offset)
                }

                override fun getCardListType() = type!!
            })
        ) {
            DatabasePagingSource(cardDb, object : Callback {
                override fun getCardListType() = type!!

                override suspend fun getDatabaseMonsterCardsInRange(top: Int, bottom: Int) =
                    cardDb.monsterDao.getAllInRange(top, bottom)

                override suspend fun getDatabaseNonMonsterCardsInRange(top: Int, bottom: Int) =
                    cardDb.nonMonsterDao.getAllInRange(top, bottom)

            })
        }.liveData
    }

    /*private fun selectCategories(list: List<DomainCard>) {
        var filters = mutableListOf(
            CardFilterCategory.Type, CardFilterCategory.Race,
            CardFilterCategory.Attribute
        )

        if (type == CardType.NON_MONSTER) filters.remove(CardFilterCategory.Attribute)

        var initial = list[0]

        if (type == CardType.MONSTER) {
            list.forEach {
                if (filters.isNotEmpty()) {
                    if (CardFilter.isEffectMonster(initial.type) != CardFilter.isEffectMonster(it.type))
                        filters.remove(CardFilterCategory.Type)
                    if (initial.race != it.race) filters.remove(CardFilterCategory.Race)
                    it as DomainCard.DomainMonsterCard
                    if ((initial as DomainCard.DomainMonsterCard).attribute != it.attribute)
                        filters.remove(CardFilterCategory.Attribute)
                    initial = it
                }
            }
        } else {
            filters = if (
                initial.type.lowercase(Locale.getDefault())
                == CardFilterCategory.Spell.query.lowercase(Locale.getDefault())
            ) mutableListOf(
                CardFilterCategory.Spell
            ) else mutableListOf(CardFilterCategory.Trap)
        }

        filters.forEach { connectivityResolver.selectCategoryOnOfflineLoad(it.name) }
    }*/
}

/*interface ConnectivityResolver {

    fun selectCategoryOnOfflineLoad(category: String)

    fun hasSelectedCategory(): Boolean
}*/

interface Callback {

    suspend fun getNetworkCards(offset: Int): Response {
        return Response(listOf())
    }

    suspend fun getDatabaseMonsterCardsInRange(top: Int, bottom: Int)
            : List<DatabaseMonsterCard>? = null

    suspend fun getDatabaseNonMonsterCardsInRange(top: Int, bottom: Int)
            : List<DatabaseNonMonsterCard>? = null

    fun getCardListType(): CardType
}