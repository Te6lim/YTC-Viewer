package com.te6lim.ytcviewer.cardFilters

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.te6lim.ytcviewer.*
import com.te6lim.ytcviewer.resources.CardFilterCategory
import com.te6lim.ytcviewer.resources.attributeIconResources
import com.te6lim.ytcviewer.resources.raceIconResources

class FilterSelectionViewModel(private val category: String) : ViewModel() {

    companion object {

        private fun getFilterBackgrounds(category: CardFilterCategory): HashMap<String, Int> {
            return when (category) {
                CardFilterCategory.Type -> typeFiltersBackgrounds

                CardFilterCategory.Race -> raceFiltersBackgrounds

                CardFilterCategory.Attribute -> attributeBackgrounds

                CardFilterCategory.Level -> levelFiltersBackgrounds

                CardFilterCategory.Spell -> spellFiltersBackgrounds

                CardFilterCategory.Trap -> trapFiltersBackgrounds
            }
        }

        fun getRaceIcon(race: String): Int {
            return raceIconResources[race] ?: R.drawable.poker
        }

        fun getAttributeIcon(attribute: String): Int {
            return attributeIconResources[attribute]!!
        }

        fun getLevelOrRankIcon(type: String): Int {
            return if (type == "XYZ Monster") R.drawable.rank
            else R.drawable.level
        }
    }

    private val types = typeFilters

    private val races = raceFilters

    private val attributes = attributeFilters

    private val levels = levelFilters

    private val spells = spellFilters

    private val traps = trapFilters

    private val _filterCategory = MutableLiveData<String>()
    val filterCategory: LiveData<String>
        get() = _filterCategory

    private val _filters = MutableLiveData<List<CardFilter>>(listOf())
    val filters: LiveData<List<CardFilter>>
        get() = _filters

    private val selectedFilters = mutableListOf<CardFilter>()

    init {
        _filterCategory.value = category
        _filters.value = getFilters(CardFilterCategory.valueOf(category))
    }

    fun getBackgroundsForFilters(): HashMap<String, Int> {
        return getFilterBackgrounds(CardFilterCategory.valueOf(category))
    }

    private fun getFilters(category: CardFilterCategory): List<CardFilter> {
        return when (category) {
            CardFilterCategory.Type -> types

            CardFilterCategory.Race -> races

            CardFilterCategory.Attribute -> attributes

            CardFilterCategory.Level -> levels

            CardFilterCategory.Spell -> spells

            CardFilterCategory.Trap -> traps
        }
    }

    fun addFilterToSelected(filter: CardFilter) {
        selectedFilters.add(filter)
    }

    fun removeFilterFromSelected(filter: CardFilter) {
        selectedFilters.remove(filter)
    }

    fun selectedFilters(): List<CardFilter> {
        return selectedFilters.toList()
    }

}

@Suppress("UNCHECKED_CAST")
class FilterSelectionViewModelFactory(private val category: String) : ViewModelProvider.Factory {
    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(FilterSelectionViewModel::class.java))
            return FilterSelectionViewModel(category) as T
        else throw IllegalArgumentException("Unknown ViewModel class")
    }
}