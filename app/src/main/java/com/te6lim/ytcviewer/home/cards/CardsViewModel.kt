package com.te6lim.ytcviewer.home.cards

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Transformations
import androidx.lifecycle.ViewModel
import com.te6lim.ytcviewer.filters.FilterSelectionViewModel
import com.te6lim.ytcviewer.network.NetworkCard
import kotlin.collections.List
import kotlin.collections.Map
import kotlin.collections.MutableMap
import kotlin.collections.isNotEmpty
import kotlin.collections.mutableMapOf
import kotlin.collections.set
import kotlin.collections.toMutableMap
import kotlin.collections.toTypedArray

class CardsViewModel : ViewModel() {

    private val _cards = MutableLiveData<List<NetworkCard>>()
    val cards: LiveData<List<NetworkCard>>
        get() = _cards

    private val selectedFilters = MutableLiveData<MutableMap<String, Array<String>>>()

    val filterTransformation = Transformations.map(selectedFilters) {
        getProperties()
    }

    private val _checkedCategories =
        MutableLiveData<Map<String, FilterSelectionViewModel.CardFilterCategory>>(mutableMapOf())

    val checkedCategories: LiveData<Map<String, FilterSelectionViewModel.CardFilterCategory>>
        get() = _checkedCategories

    var lastChecked: String? = null
        private set

    private var selectedTypeFilters = arrayOf<String>()

    private var selectedRaceFilters = arrayOf<String>()

    private var selectedAttributeFilters = arrayOf<String>()

    fun getPropertiesWithSearch(key: String) {

    }

    fun getProperties() {

    }

    fun addCategoryToChecked(category: String) {
        val map = _checkedCategories.value!!.toMutableMap()
        map[category] = FilterSelectionViewModel.CardFilterCategory.valueOf(category)
        lastChecked = category
        _checkedCategories.value = map
    }

    fun removeCategoryFromChecked(category: String) {
        val map = _checkedCategories.value!!.toMutableMap()
        map.remove(category)

        when (FilterSelectionViewModel.CardFilterCategory.valueOf(category)) {
            FilterSelectionViewModel.CardFilterCategory.Type ->
                selectedTypeFilters = arrayOf()

            FilterSelectionViewModel.CardFilterCategory.Race ->
                selectedRaceFilters = arrayOf()

            FilterSelectionViewModel.CardFilterCategory.Attribute ->
                selectedAttributeFilters = arrayOf()

            else -> throw IllegalArgumentException()
        }

        _checkedCategories.value = map

        removeFilter(FilterSelectionViewModel.CardFilterCategory.valueOf(category).query)
    }

    fun removeAllCheckedCategory() {
        selectedTypeFilters = arrayOf()
        selectedRaceFilters = arrayOf()
        selectedAttributeFilters = arrayOf()
        _checkedCategories.value = mutableMapOf()
    }

    fun setSelectedFilter(filters: List<String>) {
        lastChecked?.let {
            when (FilterSelectionViewModel.CardFilterCategory.valueOf(it)) {
                FilterSelectionViewModel.CardFilterCategory.Type -> {
                    selectedTypeFilters = filters.toTypedArray()
                }

                FilterSelectionViewModel.CardFilterCategory.Race -> {
                    selectedRaceFilters = filters.toTypedArray()
                }

                FilterSelectionViewModel.CardFilterCategory.Attribute -> {
                    selectedAttributeFilters = filters.toTypedArray()
                }

                FilterSelectionViewModel.CardFilterCategory.Spell -> {
                    selectedRaceFilters = filters.toTypedArray()
                }

                FilterSelectionViewModel.CardFilterCategory.Trap -> {
                    selectedRaceFilters = filters.toTypedArray()
                }
            }

            val map = mutableMapOf<String, Array<String>>()
            if (selectedTypeFilters.isNotEmpty())
                map[FilterSelectionViewModel.CardFilterCategory.Type.query] = selectedTypeFilters

            if (selectedRaceFilters.isNotEmpty())
                map[FilterSelectionViewModel.CardFilterCategory.Race.query] = selectedRaceFilters

            if (selectedAttributeFilters.isNotEmpty())
                map[FilterSelectionViewModel.CardFilterCategory.Attribute.query] =
                    selectedAttributeFilters

            selectedFilters.value = map
        }
    }

    private fun removeFilter(key: String) {
        selectedFilters.value = selectedFilters.value!!.apply { remove(key) }
    }
}