package com.te6lim.ytcviewer.cardDetails

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.te6lim.ytcviewer.database.Card
import com.te6lim.ytcviewer.database.CardDao
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class CardDetailsViewModel(private val database: CardDao, val card: Card) : ViewModel() {

    val favoriteCard = database.getCard(card.networkId)

    fun addToFavorite() {
        viewModelScope.launch {
            withContext(Dispatchers.IO) {
                database.insert(card.apply { favourite = true })
            }
        }
    }

    fun removeCardFromFavorite() {
        viewModelScope.launch {
            withContext(Dispatchers.IO) { database.deleteCard(card.apply { favourite = false }.networkId) }
        }
    }

    fun isCardFavorite(): Boolean {
        favoriteCard.value?.let {
            return it.favourite
        } ?: return false
    }

}

class CardDetailsViewModelFactory(private val database: CardDao, private val card: Card) : ViewModelProvider
.Factory {
    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(CardDetailsViewModel::class.java))
            return CardDetailsViewModel(database, card) as T
        throw IllegalArgumentException("unknown view model class")
    }

}