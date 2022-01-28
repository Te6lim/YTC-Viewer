package com.te6lim.ytcviewer.home

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

class HomeViewModel : ViewModel() {

    //Test Navigation
    private val _navigateToDetailScreen = MutableLiveData<Boolean>()
    val navigateToDetailScreen: LiveData<Boolean>
        get() = _navigateToDetailScreen

    fun setNavigateToDetailScreen(value: Boolean) {
        _navigateToDetailScreen.value = value
    }
}