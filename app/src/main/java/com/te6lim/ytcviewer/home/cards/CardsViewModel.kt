package com.te6lim.ytcviewer.home.cards

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.te6lim.ytcviewer.database.CardDatabase
import com.te6lim.ytcviewer.filters.CardFilter
import com.te6lim.ytcviewer.filters.CardFilterCategory
import com.te6lim.ytcviewer.home.SortItem
import com.te6lim.ytcviewer.network.Response
import com.te6lim.ytcviewer.network.YtcApi
import kotlinx.coroutines.Deferred

class CardsViewModel(db: CardDatabase) : ViewModel() {

    enum class CardType {
        MonsterCard, NonMonsterCard
    }

    private val _selectedChips = MutableLiveData<Map<String, Boolean>>()
    val selectedChips: LiveData<Map<String, Boolean>>
        get() = _selectedChips

    private val _selectedCardFilters = MutableLiveData<Map<CardFilterCategory, List<CardFilter>>>()
    val selectedCardFilters: LiveData<Map<CardFilterCategory, List<CardFilter>>>
        get() = _selectedCardFilters

    var sortMethod: SortItem? = null

    private var cardListType = CardType.MonsterCard
        private set

    init {
        val map = mutableMapOf<String, Boolean>()
        for (category in CardFilterCategory.values()) {
            map[category.name] = false
        }
        _selectedChips.value = map
    }

    fun addFiltersToSelected(category: CardFilterCategory, list: List<CardFilter>) {
        val map = _selectedCardFilters.value?.toMutableMap() ?: mutableMapOf()
        if (!map.contains(category)) map[category] = list
        else map.replace(category, list)
        _selectedCardFilters.value = map
    }

    fun toggleChip(chipName: String): Boolean {
        val map = when (chipName) {
            CardFilterCategory.Spell.name -> {
                cardListType = CardType.NonMonsterCard
                toggleSpellOrTrap(chipName)
            }

            CardFilterCategory.Trap.name -> {
                cardListType = CardType.NonMonsterCard
                toggleSpellOrTrap(chipName)
            }

            else -> {
                cardListType = CardType.MonsterCard
                toggleNonSpellAndTrap(chipName)
            }
        }
        _selectedChips.value = map
        updateFilters()?.let { _selectedCardFilters.value = it }
        return map[chipName]!!
    }

    private fun updateFilters(): Map<CardFilterCategory, List<CardFilter>>? {
        val selected = _selectedCardFilters.value?.toMutableMap()
        selected?.let {
            for (k in selectedChips.value!!.keys) {
                if (!selectedChips.value!![k]!!) it.remove(CardFilterCategory.get(k))
            }
        }
        return selected
    }

    private fun toggleNonSpellAndTrap(chipName: String): Map<String, Boolean> {
        val categories = _selectedChips.value!!.toMutableMap()

        if (categories[CardFilterCategory.Spell.name]!!) {
            categories[CardFilterCategory.Spell.name] = false
        }

        if (categories[CardFilterCategory.Trap.name]!!) {
            categories[CardFilterCategory.Trap.name] = false
        }

        categories[chipName] = !categories[chipName]!!

        return categories

    }

    private fun toggleSpellOrTrap(chipName: String): Map<String, Boolean> {
        val categories = _selectedChips.value!!.toMutableMap()
        if (!categories[chipName]!!) {
            for (s in categories.keys) {
                categories[s] = s == chipName
            }
            categories[chipName] = true
        } else {
            categories[chipName] = false
        }

        return categories
    }

    fun switchChip(chipName: String, switch: Boolean) {
        val categories = _selectedChips.value!!.toMutableMap()

        when (chipName) {
            CardFilterCategory.Spell.name, CardFilterCategory.Trap.name -> {
                if (switch) {
                    for (s in categories.keys) {
                        categories[s] = s == chipName
                    }
                }
                categories[chipName] = switch
                cardListType = CardType.NonMonsterCard
            }

            else -> {
                if (categories[CardFilterCategory.Spell.name]!!) {
                    categories[CardFilterCategory.Spell.name] = false
                }

                if (categories[CardFilterCategory.Trap.name]!!) {
                    categories[CardFilterCategory.Trap.name] = false
                }

                categories[chipName] = switch
                cardListType = CardType.MonsterCard
            }
        }
        _selectedChips.value = categories
        updateFilters()?.let { _selectedCardFilters.value = it }
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

    private fun getCards(offset: Int): Deferred<Response> {
        if (cardListType == CardType.MonsterCard) {
            return when (selectedCardFilters.value!!.size) {
                1 -> {
                    val mapQueries = getMapQueries(1)
                    YtcApi.retrofitService.getMonsterCardsAsync(mapQueries[0], offset = offset)
                }

                2 -> {
                    val mapQueries = getMapQueries(2)
                    YtcApi.retrofitService.getMonsterCardsAsync(mapQueries[0], mapQueries[1], offset = offset)
                }

                3 -> {
                    val mapQueries = getMapQueries(3)
                    YtcApi.retrofitService.getMonsterCardsAsync(
                        mapQueries[0], mapQueries[1], mapQueries[2], offset = offset
                    )
                }

                else -> {
                    val mapQueries = getMapQueries(4)
                    YtcApi.retrofitService.getMonsterCardsAsync(
                        mapQueries[0], mapQueries[1], mapQueries[2], mapQueries[3], offset = offset
                    )
                }
            }
        } else {
            val mapQueries = getMapQueries(2)
            return YtcApi.retrofitService.getNonMonsterCardsAsync(
                mapQueries[0], mapQueries[1], offset = offset
            )
        }
    }

    fun getMapQueries(size: Int): List<Map<String, String>> {
        val queries = mutableListOf<Map<String, String>>()
        val keys = selectedCardFilters.value!!.keys.toList()
        for (i in 1..size) {
            queries.add(
                mapOf(
                    Pair(
                        keys[i].query, selectedCardFilters.value!![keys[i]]!!
                            .stringFormatForNetworkQuery()
                    )
                )
            )
        }
        return queries
    }
}

class CardsViewModelFactory(private val db: CardDatabase) : ViewModelProvider.Factory {

    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(CardsViewModel::class.java)) {
            return CardsViewModel(db) as T
        } else throw IllegalArgumentException()
    }
}