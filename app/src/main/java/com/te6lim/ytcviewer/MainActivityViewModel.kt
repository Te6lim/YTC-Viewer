package com.te6lim.ytcviewer

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

class MainActivityViewModel : ViewModel() {

    private val _isDarkThemeActive = MutableLiveData(true)
    val isDarkThemeActive: LiveData<Boolean>
        get() = _isDarkThemeActive

    fun setDarkThemeActive(value: Boolean) {
        _isDarkThemeActive.value = value
    }
}