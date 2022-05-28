package com.te6lim.ytcviewer.home.favorites

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.te6lim.ytcviewer.database.CardDao

class FavoritesViewModel(private val database: CardDao) : ViewModel() {
    val favoriteCards = database.getFavorites()
}

class FavoritesViewModelFactory(private val database: CardDao) : ViewModelProvider.Factory {
    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(FavoritesViewModel::class.java))
            return FavoritesViewModel(database) as T
        throw IllegalArgumentException("Unknown view model class")
    }

}