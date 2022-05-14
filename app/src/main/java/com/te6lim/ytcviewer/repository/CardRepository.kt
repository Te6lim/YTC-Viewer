package com.te6lim.ytcviewer.repository

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.paging.ExperimentalPagingApi
import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import com.te6lim.ytcviewer.database.CardDatabase
import com.te6lim.ytcviewer.database.DatabaseCard
import com.te6lim.ytcviewer.filters.CardFilter
import com.te6lim.ytcviewer.filters.CardFilterCategory
import com.te6lim.ytcviewer.home.SortItem
import com.te6lim.ytcviewer.home.cards.CardPagingSource
import com.te6lim.ytcviewer.home.cards.CardsViewModel
import com.te6lim.ytcviewer.network.PAGE_SIZE
import com.te6lim.ytcviewer.network.Response
import com.te6lim.ytcviewer.network.YtcApi
import kotlinx.coroutines.flow.Flow

class CardRepository(private val db: CardDatabase, private val repoCallback: RepoCallback) {

    private val _isEmpty = MutableLiveData<Boolean>()
    val isEmpty: LiveData<Boolean>
        get() = _isEmpty

    private suspend fun getCards(
        selectedCardFilters: Map<CardFilterCategory, List<CardFilter>>, sortQuery: String, offset: Int
    ): Response {
        val mapQueries = getMapQueries(selectedCardFilters)
        val selectedChips = repoCallback.selectedCategories()
        val deferred = if (repoCallback.getCardResponseType() == CardsViewModel.CardType.MonsterCard) {
            when (selectedCardFilters.size) {
                1 -> {
                    YtcApi.retrofitService.getCardsAsync(
                        mapQueries[0], offset = offset, sort = sortQuery
                    )
                }

                2 -> {
                    YtcApi.retrofitService.getCardsAsync(
                        mapQueries[0], mapQueries[1], offset = offset, sort = sortQuery
                    )
                }

                3 -> {
                    YtcApi.retrofitService.getCardsAsync(
                        mapQueries[0], mapQueries[1], mapQueries[2], offset = offset, sort = sortQuery
                    )
                }

                else -> {
                    YtcApi.retrofitService.getCardsAsync(
                        mapQueries[0], mapQueries[1], mapQueries[2], mapQueries[3], offset = offset,
                        sort = sortQuery
                    )
                }
            }
        } else {
            if (selectedChips[CardFilterCategory.Spell.name] == true) {
                YtcApi.retrofitService.getCardsAsync(
                    mapOf(Pair("type", CardFilterCategory.Spell.name)), mapQueries[0], offset = offset,
                    sort = sortQuery
                )
            } else {
                YtcApi.retrofitService.getCardsAsync(
                    mapOf(Pair("type", CardFilterCategory.Trap.name)), mapQueries[0], offset = offset,
                    sort = sortQuery
                )
            }
        }

        return deferred.await()
    }

    private suspend fun getCardsBySearchKey(searchKey: String, sortQuery: String, offset: Int): Response {
        return YtcApi.retrofitService.getCardsBySearchAsync(
            searchKey, sort = sortQuery, offset = offset
        ).await()
    }

    private fun getMapQueries(
        selectedCardFilters: Map<CardFilterCategory, List<CardFilter>>
    ): List<Map<String, String>> {
        val queries = mutableListOf<Map<String, String>>()
        for (key in selectedCardFilters.keys) {
            queries.add(
                mapOf(Pair(key.query, selectedCardFilters[key]!!.stringFormatForNetworkQuery()))
            )
        }
        return queries
    }

    private fun List<CardFilter>.stringFormatForNetworkQuery(): String {
        val formattedStringBuilder = StringBuilder()
        with(formattedStringBuilder) {
            for (filter in this@stringFormatForNetworkQuery) {
                apply {
                    append(filter.name)
                    append(",")
                }

            }
            deleteCharAt(length - 1)
            return this.toString()
        }
    }

    @OptIn(ExperimentalPagingApi::class)
    fun getCardStream(
        selectedCardFilters: Map<CardFilterCategory, List<CardFilter>>? = null, searchKey: String?, sortType:
        SortItem
    ): Flow<PagingData<DatabaseCard>> {
        return Pager(config = PagingConfig(pageSize = PAGE_SIZE),
            pagingSourceFactory = {
                CardPagingSource(sortType.isAsc, object : CardPagingSource.Callback {

                    override suspend fun getNetworkCardsAsync(offset: Int): Response {
                        return if (selectedCardFilters != null)
                            getCards(
                                selectedCardFilters, sortType.query, offset
                            )
                        else getCardsBySearchKey(searchKey ?: "", sortType.query, offset)
                    }

                    override fun setIsDataEmpty(value: Boolean) {
                        if (_isEmpty.value != value) _isEmpty.value = value
                    }
                })
            }).flow
    }

    interface RepoCallback {
        fun getCardResponseType(): CardsViewModel.CardType

        fun selectedCategories(): Map<String, Boolean>

        fun sortType(): SortItem
    }

}