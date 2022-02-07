package com.te6lim.ytcviewer.home

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

class HomeViewModel : ViewModel() {

    //Test Navigation
    private val _navigateToDetailScreen = MutableLiveData<Boolean>()
    val navigateToDetailScreen: LiveData<Boolean>
        get() = _navigateToDetailScreen

    private val _searchBarClicked = MutableLiveData<Boolean>()
    val searchBarClicked: LiveData<Boolean>
        get() = _searchBarClicked

    fun setNavigateToDetailScreen(value: Boolean) {
        _navigateToDetailScreen.value = value
    }

    fun setSearchBarClicked(value: Boolean) {
        _searchBarClicked.value = value
    }
}