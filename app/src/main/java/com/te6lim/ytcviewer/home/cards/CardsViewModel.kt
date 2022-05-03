package com.te6lim.ytcviewer.home.cards

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.te6lim.ytcviewer.database.CardDatabase
import com.te6lim.ytcviewer.filters.CardFilter
import com.te6lim.ytcviewer.filters.CardFilterCategory

class CardsViewModel(db: CardDatabase) : ViewModel() {

    private val _selectedChips = MutableLiveData<Map<String, Boolean>>()
    val selectedChips: LiveData<Map<String, Boolean>>
        get() = _selectedChips

    private val _selectedFilters = MutableLiveData<Map<CardFilterCategory, List<CardFilter>>>()
    val selectedCardFilters: LiveData<Map<CardFilterCategory, List<CardFilter>>>
        get() = _selectedFilters

    init {
        val map = mutableMapOf<String, Boolean>()
        for (category in CardFilterCategory.values()) {
            map[category.name] = false
        }
        _selectedChips.value = map
    }

    fun addFiltersToSelected(category: CardFilterCategory, list: List<CardFilter>) {
        val map = _selectedFilters.value?.toMutableMap() ?: mutableMapOf()
        if (!map.contains(category)) map[category] = list
        else map.replace(category, list)
        _selectedFilters.value = map
    }

    fun toggleChip(chipName: String): Boolean {
        val map = when (chipName) {
            CardFilterCategory.Spell.name -> {
                toggleSpellOrTrap(chipName)
            }

            CardFilterCategory.Trap.name -> {
                toggleSpellOrTrap(chipName)
            }

            else -> {
                toggleNonSpellAndTrap(chipName)
            }
        }
        _selectedChips.value = map
        filteredFilters()?.let { _selectedFilters.value = it }
        return map[chipName]!!
    }

    private fun filteredFilters(): Map<CardFilterCategory, List<CardFilter>>? {
        val selected = _selectedFilters.value?.toMutableMap()
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
            }

            else -> {
                if (categories[CardFilterCategory.Spell.name]!!) {
                    categories[CardFilterCategory.Spell.name] = false
                }

                if (categories[CardFilterCategory.Trap.name]!!) {
                    categories[CardFilterCategory.Trap.name] = false
                }

                categories[chipName] = switch
            }
        }
        _selectedChips.value = categories
        filteredFilters()?.let { _selectedFilters.value = it }
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