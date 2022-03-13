package com.te6lim.ytcviewer.home.cards

import androidx.lifecycle.*
import com.te6lim.ytcviewer.filters.FilterSelectionViewModel
import com.te6lim.ytcviewer.network.NetworkCard
import com.te6lim.ytcviewer.network.NetworkStatus
import com.te6lim.ytcviewer.network.Response
import com.te6lim.ytcviewer.network.YtcApi
import kotlinx.coroutines.Deferred
import kotlinx.coroutines.launch

class CardsViewModel : ViewModel() {

    private val _cards = MutableLiveData<List<NetworkCard>>()
    val cards: LiveData<List<NetworkCard>>
        get() = _cards

    private val _checkedCategories =
        MutableLiveData<Map<String, FilterSelectionViewModel.CardFilterCategory>>(mutableMapOf())

    val checkedCategories: LiveData<Map<String, FilterSelectionViewModel.CardFilterCategory>>
        get() = _checkedCategories

    var lastChecked: String? = null
        private set

    private val selectedFilters = MutableLiveData<MutableMap<String, Array<String>>>()

    val filterTransformation = Transformations.map(selectedFilters) {
        getProperties(it)
        it.toMap()
    }

    private var selectedTypeFilters = arrayOf<String>()

    private var selectedRaceFilters = arrayOf<String>()

    private var selectedAttributeFilters = arrayOf<String>()

    private val _networkStatus = MutableLiveData<NetworkStatus>()
    val networkStatus: LiveData<NetworkStatus>
        get() = _networkStatus

    fun getPropertiesWithSearch(key: String) {
        viewModelScope.launch {

            val cardsDeferred: Deferred<Response> =
                YtcApi.retrofitService.getCardsBySearchAsync(key)

            try {
                _networkStatus.value = NetworkStatus.LOADING
                _cards.value = cardsDeferred.await().data
                _networkStatus.value = NetworkStatus.DONE
            } catch (e: Exception) {
                _networkStatus.value = NetworkStatus.ERROR
            }

        }
    }

    private fun getProperties(filterMap: Map<String, Array<String>>) {
        viewModelScope.launch {

            var cardsDeferred: Deferred<Response>? = null

            val keys = filterMap.keys.toList()

            when (filterMap.size) {
                1 -> {

                    if (lastChecked == FilterSelectionViewModel.CardFilterCategory.Spell.name) {
                        cardsDeferred = YtcApi.retrofitService.getNonMonsterCardsAsync(
                            mapOf(Pair("type", "spell card")),
                            mapOf(
                                Pair(
                                    keys[0],
                                    filterMap[keys[0]]!!.formattedString()
                                )
                            )
                        )
                    } else {
                        cardsDeferred =
                            if (lastChecked == FilterSelectionViewModel.CardFilterCategory.Trap.name) {
                                YtcApi.retrofitService.getNonMonsterCardsAsync(
                                    mapOf(Pair("type", "trap card")),
                                    mapOf(
                                        Pair(
                                            keys[0],
                                            filterMap[keys[0]]!!.formattedString()
                                        )
                                    )
                                )
                            } else {
                                YtcApi.retrofitService.getCardsAsync(
                                    mapOf(
                                        Pair(
                                            keys[0],
                                            filterMap[keys[0]]!!.formattedString()
                                        )
                                    )
                                )
                            }
                    }
                }

                2 -> {
                    cardsDeferred = YtcApi.retrofitService.getCardsAsync(
                        mapOf(Pair(keys[0], filterMap[keys[0]]!!.formattedString())),
                        mapOf(Pair(keys[1], filterMap[keys[1]]!!.formattedString()))
                    )
                }

                3 -> {
                    cardsDeferred = YtcApi.retrofitService.getCardsAsync(
                        mapOf(Pair(keys[0], filterMap[keys[0]]!!.formattedString())),
                        mapOf(Pair(keys[1], filterMap[keys[1]]!!.formattedString())),
                        mapOf(Pair(keys[2], filterMap[keys[2]]!!.formattedString()))
                    )
                }
            }

            try {
                _networkStatus.value = NetworkStatus.LOADING
                _cards.value = cardsDeferred!!.await().data
                _networkStatus.value = NetworkStatus.DONE
            } catch (e: Exception) {
                _networkStatus.value = NetworkStatus.ERROR
            }
        }
    }

    private fun Array<String>.formattedString(): String {
        val arguments = StringBuilder().apply {
            for ((i, string) in this@formattedString.withIndex()) {
                append(string)
                if (i != size - 1) append(",")
            }
        }
        return arguments.toString()
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
        val x = 0
    }
}