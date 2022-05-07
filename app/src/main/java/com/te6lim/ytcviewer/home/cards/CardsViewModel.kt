package com.te6lim.ytcviewer.home.cards

import androidx.lifecycle.*
import androidx.paging.map
import com.te6lim.ytcviewer.database.CardDatabase
import com.te6lim.ytcviewer.database.toDomainCard
import com.te6lim.ytcviewer.filters.CardFilter
import com.te6lim.ytcviewer.filters.CardFilterCategory
import com.te6lim.ytcviewer.home.SortItem
import com.te6lim.ytcviewer.repository.CardRepository
import kotlinx.coroutines.flow.map

class CardsViewModel(db: CardDatabase) : ViewModel() {

    enum class CardType {
        MonsterCard, NonMonsterCard
    }

    private val _selectedChips = MutableLiveData<Map<String, Boolean>>()
    val selectedChips: LiveData<Map<String, Boolean>>
        get() = _selectedChips

    private val selectedCardFilters = MutableLiveData<Map<CardFilterCategory, List<CardFilter>>>()

    val filters = Transformations.map(selectedCardFilters) {
        repo.getCardStream(it).map { pagingData ->
            pagingData.map { it.toDomainCard() }
        }
    }

    var sortMethod: SortItem? = null

    private var cardListType = CardType.MonsterCard

    private val repo = CardRepository(db, object : CardRepository.RepoCallback {
        override fun getCardResponseType(): CardType = cardListType
        override fun selectedChips(): Map<String, Boolean> = selectedChipsCopy()
    })

    private fun selectedChipsCopy(): MutableMap<String, Boolean> {
        val chips = mutableMapOf<String, Boolean>()
        for (k in selectedChips.value!!.keys) chips[k] = selectedChips.value!![k]!!
        return chips
    }

    init {
        val map = mutableMapOf<String, Boolean>()
        for (category in CardFilterCategory.values()) {
            map[category.name] = false
        }
        _selectedChips.value = map
    }

    fun addFiltersToSelected(category: CardFilterCategory, list: List<CardFilter>) {
        val map = selectedCardFilters.value?.toMutableMap() ?: mutableMapOf()
        if (!map.contains(category)) map[category] = list
        else map.replace(category, list)
        selectedCardFilters.value = map
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
        updateFilters()?.let { selectedCardFilters.value = it }
        return map[chipName]!!
    }

    private fun updateFilters(): Map<CardFilterCategory, List<CardFilter>>? {
        val selected = selectedCardFilters.value?.toMutableMap()
        selected?.let {
            for (k in selectedChips.value!!.keys) {
                if (!selectedChips.value!![k]!!) it.remove(CardFilterCategory.get(k))
            }
        }
        return selected
    }

    private fun toggleNonSpellAndTrap(chipName: String): Map<String, Boolean> {
        val categories = _selectedChips.value!!.toMutableMap()

        if (categories[CardFilterCategory.Spell.name]!!) categories[CardFilterCategory.Spell.name] = false

        if (categories[CardFilterCategory.Trap.name]!!) categories[CardFilterCategory.Trap.name] = false

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
        updateFilters()?.let { selectedCardFilters.value = it }
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