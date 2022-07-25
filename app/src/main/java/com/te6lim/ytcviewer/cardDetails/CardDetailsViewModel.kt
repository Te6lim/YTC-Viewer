package com.te6lim.ytcviewer.cardDetails

import androidx.lifecycle.*
import com.te6lim.ytcviewer.database.Card
import com.te6lim.ytcviewer.repository.CardRepository
import kotlinx.coroutines.launch

class CardDetailsViewModel(private val repository: CardRepository, val card: Card) : ViewModel() {

    private val _favoriteCard = MutableLiveData<Card?>()
    val favoriteCard: LiveData<Card?>
        get() = _favoriteCard

    init {
        setCardAsLiveData(card.networkId)
    }

    fun addToFavorite() {
        viewModelScope.launch {
            repository.addCard(card.apply { isFavourite = true })
            _favoriteCard.postValue(card)
        }
    }

    fun removeCardFromFavorite() {
        viewModelScope.launch {
            repository.deleteCard(card.apply { isFavourite = false }.networkId)
            _favoriteCard.postValue(null)
        }
    }

    fun isCardFavorite(): Boolean {
        favoriteCard.value?.let {
            return it.isFavourite
        } ?: return false
    }

    private fun setCardAsLiveData(id: Long) {
        viewModelScope.launch {
            repository.getCard(id)?.let { _favoriteCard.postValue(it) }
        }
    }

}

class CardDetailsViewModelFactory(private val repository: CardRepository, private val card: Card) :
    ViewModelProvider
    .Factory {
    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(CardDetailsViewModel::class.java))
            return CardDetailsViewModel(repository, card) as T
        throw IllegalArgumentException("unknown view model class")
    }

}