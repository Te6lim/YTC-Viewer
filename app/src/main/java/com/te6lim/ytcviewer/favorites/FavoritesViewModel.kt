package com.te6lim.ytcviewer.favorites

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Transformations
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.te6lim.ytcviewer.repository.CardRepository
import java.util.*

class FavoritesViewModel(repository: CardRepository) : ViewModel() {

    private val searchKey = MutableLiveData("")

    private val _favorites = Transformations.switchMap(searchKey) {
        repository.savedCards()
    }

    val favorites = Transformations.map(_favorites) { cards ->
        cards?.filter {
            it.isFavourite
        }?.filter { card ->
            card.name.lowercase(Locale.getDefault())
                .contains(searchKey.value!!.lowercase(Locale.getDefault()))
        }
    }

    fun setSearchKey(key: String) {
        searchKey.value = key
    }
}

class FavoritesViewModelProvider(private val repository: CardRepository) : ViewModelProvider.Factory {
    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(FavoritesViewModel::class.java))
            return FavoritesViewModel(repository) as T
        throw IllegalArgumentException("Unknown viewModel class")
    }

}