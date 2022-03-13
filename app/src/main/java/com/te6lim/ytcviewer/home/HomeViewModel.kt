package com.te6lim.ytcviewer.home

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

class HomeViewModel : ViewModel() {

    private val _searchBarClicked = MutableLiveData<Boolean>()
    val searchBarClicked: LiveData<Boolean>
        get() = _searchBarClicked

    private val _checkedChipName = MutableLiveData<String?>()
    val checkedChipName: LiveData<String?>
        get() = _checkedChipName

    private val _searchKey = MutableLiveData<String?>()
    val searchKey: LiveData<String?>
        get() = _searchKey


    private val _filterList = MutableLiveData<List<String>?>()
    val filterList: LiveData<List<String>?>
        get() = _filterList

    fun setSearchBarClicked(value: Boolean) {
        _searchBarClicked.value = value
    }

    fun setChipChecked(value: String?) {
        _checkedChipName.value = value
    }

    fun setFilterList(value: List<String>?) {
        _filterList.value = value
    }

    fun setSearchKey(value: String?) {
        _searchKey.value = value
    }
}