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

    var lastChecked: String? = null

    fun setSearchBarClicked(value: Boolean) {
        _searchBarClicked.value = value
    }

    fun setChipChecked(value: String?) {
        value?.let {
            lastChecked = it
        }
        _checkedChipName.value = value
    }
}