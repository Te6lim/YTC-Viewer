package com.te6lim.ytcviewer.home.cards

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.te6lim.ytcviewer.database.CardDatabase

class CardsViewModel(db: CardDatabase) : ViewModel() {
}

class CardsViewModelFactory(private val db: CardDatabase) : ViewModelProvider.Factory {
    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(CardsViewModel::class.java)) {
            return CardsViewModel(db) as T
        } else throw IllegalArgumentException();
    }


}