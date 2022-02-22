package com.te6lim.ytcviewer.home

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

class HomeViewModel : ViewModel() {

    private val _searchBarClicked = MutableLiveData<Boolean>()
    val searchBarClicked: LiveData<Boolean>
        get() = _searchBarClicked

    private val _checkedChipName = MutableLiveData<Pair<String, String>?>()
    val checkedChipName: LiveData<Pair<String, String>?>
        get() = _checkedChipName

    fun setSearchBarClicked(value: Boolean) {
        _searchBarClicked.value = value
    }

    fun setChipChecked(value: Pair<String, String>?) {
        _checkedChipName.value = value
    }
}