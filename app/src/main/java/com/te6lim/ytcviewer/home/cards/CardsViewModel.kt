package com.te6lim.ytcviewer.home.cards

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.te6lim.ytcviewer.network.YtcApi
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class CardsViewModel : ViewModel() {

    private val _propertiesString = MutableLiveData<String>()
    val propertiesString: LiveData<String>
        get() = _propertiesString

    private val _checkedCategories =
        MutableLiveData<Map<String, FilterSelectionViewModel.CardFilterCategory>>(
            mutableMapOf()
        )
    val checkedCategories: LiveData<Map<String, FilterSelectionViewModel.CardFilterCategory>>
        get() = _checkedCategories

    private val _currentHasSelectedFilters = MutableLiveData(false)
    val currentHasSelectedFilters: LiveData<Boolean>
        get() = _currentHasSelectedFilters

    private var selectedTypeFilters = listOf<String>()

    private var selectedRaceFilters = listOf<String>()

    private var selectedAttributeFilters = listOf<String>()


    private fun getProperties() {
        YtcApi.retrofitService.getCardsAsync(
            selectedTypeFilters, selectedRaceFilters, selectedAttributeFilters
        ).enqueue(object : Callback<String> {
            override fun onResponse(call: Call<String>, response: Response<String>) {
                _propertiesString.value = "success"
            }

            override fun onFailure(call: Call<String>, t: Throwable) {
                _propertiesString.value = t.message
            }
        })
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
                selectedTypeFilters = mutableListOf()

            FilterSelectionViewModel.CardFilterCategory.Race ->
                selectedRaceFilters = mutableListOf()

            FilterSelectionViewModel.CardFilterCategory.Attribute ->
                selectedAttributeFilters = mutableListOf()

            else -> throw IllegalArgumentException()
        }

        _checkedCategories.value = map
    }

    fun removeAllCheckedCategory() {
        selectedTypeFilters = mutableListOf()
        selectedRaceFilters = mutableListOf()
        selectedAttributeFilters = mutableListOf()
        _checkedCategories.value = mutableMapOf()
    }

    fun setSelectedFilter(
        category: FilterSelectionViewModel.CardFilterCategory, filters: List<String>
    ) {
        when (category) {
            FilterSelectionViewModel.CardFilterCategory.Type -> {
                selectedTypeFilters = filters
                _currentHasSelectedFilters.value = true
            }

            FilterSelectionViewModel.CardFilterCategory.Race -> {
                selectedRaceFilters = filters
                _currentHasSelectedFilters.value = true
            }

            FilterSelectionViewModel.CardFilterCategory.Attribute -> {
                selectedAttributeFilters = filters
                _currentHasSelectedFilters.value = true
            }

            FilterSelectionViewModel.CardFilterCategory.Spell -> {
                selectedRaceFilters = filters
                _currentHasSelectedFilters.value = true
            }

            FilterSelectionViewModel.CardFilterCategory.Trap -> {
                selectedRaceFilters = filters
                _currentHasSelectedFilters.value = true
            }
        }
    }

    fun setCurrentHasSelectedFilters(value: Boolean) {
        _currentHasSelectedFilters.value = value
    }
}