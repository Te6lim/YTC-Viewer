package com.te6lim.ytcviewer.repository

import androidx.paging.ExperimentalPagingApi
import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import com.te6lim.ytcviewer.database.CardDatabase
import com.te6lim.ytcviewer.database.DatabaseCard
import com.te6lim.ytcviewer.filters.CardFilter
import com.te6lim.ytcviewer.filters.CardFilterCategory
import com.te6lim.ytcviewer.home.cards.CardRemoteMediator
import com.te6lim.ytcviewer.home.cards.CardsViewModel
import com.te6lim.ytcviewer.network.PAGE_SIZE
import com.te6lim.ytcviewer.network.Response
import com.te6lim.ytcviewer.network.YtcApi
import kotlinx.coroutines.Deferred
import kotlinx.coroutines.flow.Flow

class CardRepository(private val db: CardDatabase, private val repoCallback: RepoCallback) {

    private fun getCardsAsync(selectedCardFilters: Map<CardFilterCategory, List<CardFilter>>, offset: Int):
            Deferred<Response> {
        val mapQueries = getMapQueries(selectedCardFilters)
        val selectedChips = repoCallback.selectedChips()
        if (repoCallback.getCardResponseType() == CardsViewModel.CardType.MonsterCard) {
            return when (selectedCardFilters.size) {
                1 -> {
                    YtcApi.retrofitService.getMonsterCardsAsync(mapQueries[0], offset = offset)
                }

                2 -> {
                    YtcApi.retrofitService.getMonsterCardsAsync(mapQueries[0], mapQueries[1], offset = offset)
                }

                3 -> {
                    YtcApi.retrofitService.getMonsterCardsAsync(
                        mapQueries[0], mapQueries[1], mapQueries[2], offset = offset
                    )
                }

                else -> {
                    YtcApi.retrofitService.getMonsterCardsAsync(
                        mapQueries[0], mapQueries[1], mapQueries[2], mapQueries[3], offset = offset
                    )
                }
            }
        } else {
            return if (selectedChips[CardFilterCategory.Spell.name] == true) {
                YtcApi.retrofitService.getNonMonsterCardsAsync(
                    mapOf(Pair("type", CardFilterCategory.Spell.name)), mapQueries[0], offset = offset
                )
            } else {
                YtcApi.retrofitService.getNonMonsterCardsAsync(
                    mapOf(Pair("type", CardFilterCategory.Trap.name)), mapQueries[0], offset = offset
                )
            }
        }
    }

    private fun getMapQueries(selectedCardFilters: Map<CardFilterCategory, List<CardFilter>>)
            : List<Map<String, String>> {
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
        selectedCardFilters: Map<CardFilterCategory, List<CardFilter>>
    ): Flow<PagingData<DatabaseCard>> {
        val databasePagingSource = { db.cardDao.getSource() }

        return Pager(
            config = PagingConfig(pageSize = PAGE_SIZE),
            remoteMediator = CardRemoteMediator(
                db, object : CardRemoteMediator.Callback {
                    override fun getNetworkCardsAsync(offset: Int): Deferred<Response> {
                        return getCardsAsync(selectedCardFilters, offset = offset)
                    }
                }
            ),
            pagingSourceFactory = databasePagingSource
        ).flow
    }

    interface RepoCallback {
        fun getCardResponseType(): CardsViewModel.CardType

        fun selectedChips(): Map<String, Boolean>
    }

}