package com.te6lim.ytcviewer.favorites

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Transformations
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.te6lim.ytcviewer.database.CardDao
import java.util.*

class FavoritesViewModel(private val database: CardDao) : ViewModel() {

    private val searchKey = MutableLiveData("")

    private val favorites = Transformations.switchMap(searchKey) {
        database.getFavorites()
    }

    val cards = Transformations.map(favorites) {
        it?.filter { card ->
            card.name.lowercase(
                Locale.getDefault()
            ).contains(
                searchKey.value!!.lowercase(Locale.getDefault())
            )
        }
    }

    fun setSearchKey(key: String) {
        searchKey.value = key
    }

}

class FavoritesViewModelFactory(private val database: CardDao) : ViewModelProvider.Factory {
    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(FavoritesViewModel::class.java))
            return FavoritesViewModel(database) as T
        throw IllegalArgumentException("Unknown view model class")
    }
}