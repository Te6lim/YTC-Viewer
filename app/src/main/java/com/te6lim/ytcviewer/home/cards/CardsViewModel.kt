package com.te6lim.ytcviewer.home.cards

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.te6lim.ytcviewer.network.YtcApi
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class CardsViewModel() : ViewModel() {

    private val _propertiesString = MutableLiveData<String>()
    val propertiesString: LiveData<String>
        get() = _propertiesString

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


    fun getProperties() {
        when (selectedFilters.size) {
            1 -> {
                val key = selectedFilters.keys.toList()[0]
                YtcApi.retrofitService.getCardsAsync(mapOf(Pair(key, selectedFilters[key]!!)))
                    .enqueue(object : Callback<String> {
                        override fun onResponse(call: Call<String>, response: Response<String>) {
                            _propertiesString.value = "success"
                        }

                        override fun onFailure(call: Call<String>, t: Throwable) {
                            _propertiesString.value = t.message
                        }
                    })
            }

            2 -> {
                val keys = selectedFilters.keys.toList()
                YtcApi.retrofitService.getCardsAsync(
                    mapOf(Pair(keys[0], selectedFilters[keys[0]]!!)),
                    mapOf(Pair(keys[1], selectedFilters[keys[1]]!!))
                ).enqueue(object : Callback<String> {
                    override fun onResponse(call: Call<String>, response: Response<String>) {
                        _propertiesString.value = "success"
                    }

                    override fun onFailure(call: Call<String>, t: Throwable) {
                        _propertiesString.value = t.message
                    }
                })
            }

            3 -> {
                val keys = selectedFilters.keys.toList()
                YtcApi.retrofitService.getCardsAsync(
                    mapOf(Pair(keys[0], selectedFilters[keys[0]]!!)),
                    mapOf(Pair(keys[1], selectedFilters[keys[1]]!!)),
                    mapOf(Pair(keys[2], selectedFilters[keys[2]]!!))
                ).enqueue(object : Callback<String> {
                    override fun onResponse(call: Call<String>, response: Response<String>) {
                        _propertiesString.value = "success"
                    }

                    override fun onFailure(call: Call<String>, t: Throwable) {
                        _propertiesString.value = t.message
                    }
                })
            }
        }
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
            map[FilterSelectionViewModel.CardFilterCategory.Type.name] = selectedTypeFilters

        if (selectedRaceFilters.isNotEmpty())
            map[FilterSelectionViewModel.CardFilterCategory.Race.name] = selectedRaceFilters

        if (selectedAttributeFilters.isNotEmpty())
            map[FilterSelectionViewModel.CardFilterCategory.Attribute.name] =
                selectedAttributeFilters

        selectedFilters = map
    }
}