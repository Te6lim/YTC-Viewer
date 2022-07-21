package com.te6lim.ytcviewer.repository

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.paging.ExperimentalPagingApi
import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import com.te6lim.ytcviewer.cardFilters.CardFilter
import com.te6lim.ytcviewer.cardList.CardPagingSource
import com.te6lim.ytcviewer.cardList.CardsViewModel
import com.te6lim.ytcviewer.database.Card
import com.te6lim.ytcviewer.model.SortType
import com.te6lim.ytcviewer.network.*
import com.te6lim.ytcviewer.resources.CardFilterCategory
import kotlinx.coroutines.flow.Flow

class CardRepository(private val remoteSource: YtcApiService) {

    private val _isEmpty = MutableLiveData<Boolean>()
    val isEmpty: LiveData<Boolean>
        get() = _isEmpty

    private val _connectionStatus = MutableLiveData<NetworkStatus>()
    val connectionStatus: LiveData<NetworkStatus>
        get() = _connectionStatus

    private var currentPagingSource: CardPagingSource? = null

    private suspend fun getCards(
        categories: Map<String, Boolean>, selectedCardFilters: Map<CardFilterCategory, List<CardFilter>>,
        responseType: CardsViewModel.CardType, sortQuery: String, offset: Int
    ): Response {
        val mapQueries = getMapQueries(selectedCardFilters)
        val deferred = if (responseType == CardsViewModel.CardType.MonsterCard) {
            when (selectedCardFilters.size) {
                1 -> {
                    remoteSource.getCardsAsync(
                        mapQueries[0], offset = offset, sort = sortQuery
                    )
                }

                2 -> {
                    remoteSource.getCardsAsync(
                        mapQueries[0], mapQueries[1], offset = offset, sort = sortQuery
                    )
                }

                3 -> {
                    remoteSource.getCardsAsync(
                        mapQueries[0], mapQueries[1], mapQueries[2], offset = offset, sort = sortQuery
                    )
                }

                4 -> {
                    remoteSource.getCardsAsync(
                        mapQueries[0], mapQueries[1], mapQueries[2], mapQueries[3], offset = offset,
                        sort = sortQuery
                    )
                }

                else -> throw IllegalArgumentException()
            }
        } else {
            if (categories[CardFilterCategory.Spell.name] == true) {
                remoteSource.getCardsAsync(
                    mapOf(Pair(CardFilterCategory.Type.query, CardFilterCategory.TypeArgumentForSpellCard)),
                    mapQueries[0], offset = offset, sort = sortQuery
                )
            } else {
                remoteSource.getCardsAsync(
                    mapOf(Pair(CardFilterCategory.Type.query, CardFilterCategory.TypeArgumentForTrapCard)),
                    mapQueries[0], offset = offset, sort = sortQuery
                )
            }
        }

        return deferred.await()
    }

    private suspend fun getCardsBySearchKey(searchKey: String, sortQuery: String, offset: Int): Response {
        return remoteSource.getCardsBySearchAsync(
            searchKey, sort = sortQuery, offset = offset
        ).await()
    }

    private fun getMapQueries(
        selectedCardFilters: Map<CardFilterCategory, List<CardFilter>>
    ): List<Map<String, String>> {
        val queries = mutableListOf<Map<String, String>>()
        for (key in selectedCardFilters.keys) {
            queries.add(mapOf(Pair(key.query, selectedCardFilters[key]!!.stringFormatForNetworkQuery())))
        }
        return queries
    }

    private fun List<CardFilter>.stringFormatForNetworkQuery(): String {
        val formattedStringBuilder = StringBuilder()
        with(formattedStringBuilder) {
            for (filter in this@stringFormatForNetworkQuery) {
                apply {
                    append(filter.query)
                    append(",")
                }

            }
            deleteCharAt(length - 1)
            return this.toString()
        }
    }

    fun resetLoadCount() {
        currentPagingSource?.let {
            it.loadCount = 0
        }
    }

    @OptIn(ExperimentalPagingApi::class)
    fun getCardStream(
        categories: Map<String, Boolean>, selectedCardFilters: Map<CardFilterCategory, List<CardFilter>> =
            mapOf(), responseType: CardsViewModel.CardType, searchKey: String = "",
        sortType: SortType
    ): Flow<PagingData<Card>> {
        val pagingSource = CardPagingSource(sortType.isAsc, object : CardPagingSource.PagingSourceCallbacks {

            override suspend fun getNetworkCardsAsync(offset: Int): Response {
                return if (!selectedCardFilters.isNullOrEmpty())
                    getCards(categories, selectedCardFilters, responseType, sortType.query, offset)
                else {
                    getCardsBySearchKey(searchKey, sortType.query, offset)
                }
            }

            override fun setIsDataEmpty(value: Boolean) {
                if (_isEmpty.value != value) _isEmpty.value = value
            }

            override fun setNetworkStatus(status: NetworkStatus) {
                _connectionStatus.value = status
            }

        })

        currentPagingSource = pagingSource
        return Pager(
            config = PagingConfig(pageSize = PAGE_SIZE), pagingSourceFactory = { pagingSource }
        ).flow
    }

}