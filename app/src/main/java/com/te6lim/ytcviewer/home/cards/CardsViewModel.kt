package com.te6lim.ytcviewer.home.cards

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.te6lim.ytcviewer.database.CardDatabase
import com.te6lim.ytcviewer.filters.CardFilterCategory

class CardsViewModel(db: CardDatabase) : ViewModel() {

    private val _selectedChips = MutableLiveData<MutableMap<String, Boolean>>()
    val selectedChips: LiveData<MutableMap<String, Boolean>>
        get() = _selectedChips

    init {
        val map = mutableMapOf<String, Boolean>()
        for (category in CardFilterCategory.values()) {
            map[category.name] = false
        }
        _selectedChips.value = map
    }

    fun toggleChip(chipName: String) {
        with(_selectedChips.value!!) {
            when (chipName) {
                CardFilterCategory.Spell.name, CardFilterCategory.Trap.name -> {
                    for (s in this.keys) {
                        this[s] = s == chipName
                    }
                }
                else -> {
                    if (this[CardFilterCategory.Spell.name]!!) this[CardFilterCategory.Spell
                        .name] = false

                    if (this[CardFilterCategory.Trap.name]!!) this[CardFilterCategory.Trap
                        .name] = false

                    this[chipName] = !this[chipName]!!
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