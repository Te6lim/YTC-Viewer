package com.te6lim.ytcviewer.home

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.te6lim.ytcviewer.home.cards.FilterSelectionViewModel

class HomeViewModel : ViewModel() {

    private val _checkedMonsterCategories =
        MutableLiveData<Map<String, FilterSelectionViewModel.CardFilterCategory>>(
            mutableMapOf()
        )
    val checkedMonsterCategories: LiveData<Map<String, FilterSelectionViewModel.CardFilterCategory>>
        get() = _checkedMonsterCategories

    private val _checkedNonMonsterCategories =
        MutableLiveData<Map<String, FilterSelectionViewModel.NonMonsterCardFilterCategory>>(
            mutableMapOf()
        )

    val checkedNonMonsterCategories: LiveData<Map<String, FilterSelectionViewModel.NonMonsterCardFilterCategory>>
        get() = _checkedNonMonsterCategories

    private val _searchBarClicked = MutableLiveData<Boolean>()
    val searchBarClicked: LiveData<Boolean>
        get() = _searchBarClicked

    fun setSearchBarClicked(value: Boolean) {
        _searchBarClicked.value = value
    }

    private val _checkedChipName = MutableLiveData<Pair<String, String>?>()
    val checkedChipName: LiveData<Pair<String, String>?>
        get() = _checkedChipName

    fun setChipChecked(value: Pair<String, String>?) {
        _checkedChipName.value = value
    }

    fun addMonsterCategoryToChecked(category: String) {
        val map = _checkedMonsterCategories.value!!.toMutableMap()
        map[category] = FilterSelectionViewModel.CardFilterCategory.valueOf(category)
        _checkedMonsterCategories.value = map
    }

    fun removeMonsterCategoryFromChecked(category: String) {
        val map = _checkedMonsterCategories.value!!.toMutableMap()
        map.remove(category)
        _checkedMonsterCategories.value = map
    }

    fun addNonMonsterCategoryToChecked(category: String) {
        val map = _checkedNonMonsterCategories.value!!.toMutableMap()
        map[category] = FilterSelectionViewModel.NonMonsterCardFilterCategory.valueOf(category)
        _checkedNonMonsterCategories.value = map
    }

    fun removeNonMonsterCategoryFromChecked(category: String) {
        val map = _checkedNonMonsterCategories.value!!.toMutableMap()
        map.remove(category)
        _checkedNonMonsterCategories.value = map
    }

    fun removeAllCheckedMonsterCategory() {
        _checkedMonsterCategories.value = mutableMapOf()
    }

    fun removeAllCheckedNonMonsterCategory() {
        _checkedNonMonsterCategories.value = mutableMapOf()
    }
}