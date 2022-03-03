package com.te6lim.ytcviewer.home.cards

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.te6lim.ytcviewer.filters.FilterSelectionViewModel
import com.te6lim.ytcviewer.models.Card
import com.te6lim.ytcviewer.models.Response
import com.te6lim.ytcviewer.network.YtcApi
import kotlinx.coroutines.Deferred
import kotlinx.coroutines.launch

class CardsViewModel : ViewModel() {

    /*private val _propertiesString = MutableLiveData<String>()
    val propertiesString: LiveData<String>
        get() = _propertiesString*/

    private val _cards = MutableLiveData<List<Card>>()
    val cards: LiveData<List<Card>>
        get() = _cards

    private val _checkedCategories =
        MutableLiveData<Map<String, FilterSelectionViewModel.CardFilterCategory>>(
            mutableMapOf()
        )
    val checkedCategories: LiveData<Map<String, FilterSelectionViewModel.CardFilterCategory>>
        get() = _checkedCategories

    private var selectedFilters = mapOf<String, Array<String>>()

    private var selectedTypeFilters = arrayOf<String>()

    private var selectedRaceFilters = arrayOf<String>()

    private var selectedAttributeFilters = arrayOf<String>()


    fun getProperties(type: String) {

        viewModelScope.launch {

            var cardsDeferred: Deferred<Response>? = null

            val keys = selectedFilters.keys.toList()

            when (selectedFilters.size) {
                1 -> {

                    if (type == FilterSelectionViewModel.CardFilterCategory.Spell.name) {
                        cardsDeferred = YtcApi.retrofitService.getCardsAsync(
                            mapOf(Pair("type", arrayOf("spell card")[0])),
                            mapOf(Pair(keys[0], getParametersString(keys[0])))
                        )
                    } else {
                        cardsDeferred =
                            if (type == FilterSelectionViewModel.CardFilterCategory.Trap.name) {
                                YtcApi.retrofitService.getCardsAsync(
                                    mapOf(Pair("type", arrayOf("trap card")[0])),
                                    mapOf(Pair(keys[0], getParametersString(keys[0])))
                                )
                            } else {
                                YtcApi.retrofitService.getCardsAsync(
                                    mapOf(Pair(keys[0], getParametersString(keys[0])))
                                )
                            }
                    }
                }

                2 -> {
                    cardsDeferred = YtcApi.retrofitService.getCardsAsync(
                        mapOf(Pair(keys[0], getParametersString(keys[0]))),
                        mapOf(Pair(keys[1], getParametersString(keys[1])))
                    )
                }

                3 -> {
                    cardsDeferred = YtcApi.retrofitService.getCardsAsync(
                        mapOf(Pair(keys[0], getParametersString(keys[0]))),
                        mapOf(Pair(keys[1], getParametersString(keys[1]))),
                        mapOf(Pair(keys[2], getParametersString(keys[2])))
                    )
                }
            }

            try {
                _cards.value = cardsDeferred!!.await().data
                val x = 2
            } catch (e: Exception) {
                e.message
            }

        }
    }

    private fun getParametersString(key: String): String {
        val keys = selectedFilters.keys.toList()
        val arguments = StringBuilder().apply {
            for ((i, string) in selectedFilters[keys[0]]!!.withIndex()) {
                append(string)
                if (i != selectedFilters[key]!!.size - 1) append(",")
            }
        }
        return arguments.toString()
    }

    fun addCategoryToChecked(category: String) {
        val map = _checkedCategories.value!!.toMutableMap()
        map[category] = FilterSelectionViewModel.CardFilterCategory.valueOf(category)
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
    }

    fun removeAllCheckedCategory() {
        selectedTypeFilters = arrayOf()
        selectedRaceFilters = arrayOf()
        selectedAttributeFilters = arrayOf()
        _checkedCategories.value = mutableMapOf()
    }

    fun setSelectedFilter(
        category: FilterSelectionViewModel.CardFilterCategory, filters: List<String>
    ) {
        when (category) {
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

        selectedFilters = map
    }
}