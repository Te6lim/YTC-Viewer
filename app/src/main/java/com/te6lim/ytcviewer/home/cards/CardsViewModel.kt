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

    fun removeFiltersFromSelected(category: CardFilterCategory) {
        val map = _selectedFilters.value?.toMutableMap()
        map?.let {
            it.remove(category)
            _selectedFilters.value = it
        }
    }

    fun toggleChip(chipName: String): Boolean {
        with(_selectedChips.value!!.toMutableMap()) {
            when (chipName) {
                CardFilterCategory.Spell.name, CardFilterCategory.Trap.name -> {
                    if (!this[chipName]!!) {
                        for (s in this.keys) {
                            this[s] = s == chipName
                        }
                        this[chipName] = true
                    } else this[chipName] = false
                }
                else -> {
                    if (this[CardFilterCategory.Spell.name]!!)
                        this[CardFilterCategory.Spell.name] = false

                    if (this[CardFilterCategory.Trap.name]!!)
                        this[CardFilterCategory.Trap.name] = false

                    this[chipName] = !this[chipName]!!
                }
            }
            _selectedChips.value = this
            return this[chipName]!!
        }
    }

    fun switchChip(chipName: String, switch: Boolean) {
        with(_selectedChips.value!!.toMutableMap()) {
            when (chipName) {
                CardFilterCategory.Spell.name, CardFilterCategory.Trap.name -> {
                    if (switch) {
                        for (s in this.keys) {
                            this[s] = s == chipName
                        }
                    }
                    this[chipName] = switch
                }
                else -> {
                    if (this[CardFilterCategory.Spell.name]!!) this[CardFilterCategory.Spell
                        .name] = false

                    if (this[CardFilterCategory.Trap.name]!!) this[CardFilterCategory.Trap
                        .name] = false

                    this[chipName] = switch
                }
            }
            _selectedChips.value = this
        }
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