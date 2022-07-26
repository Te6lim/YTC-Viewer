package com.te6lim.ytcviewer.cardDetails

import androidx.lifecycle.*
import com.te6lim.ytcviewer.database.Card
import com.te6lim.ytcviewer.repository.CardRepository
import kotlinx.coroutines.launch

class CardDetailsViewModel(private val repository: CardRepository, private val cardId: Long) : ViewModel() {

    private val _databaseCard = MutableLiveData<Card?>()
    val databaseCard: LiveData<Card?>
        get() = _databaseCard

    init {
        viewModelScope.launch {
            _databaseCard.postValue(repository.getCard(cardId))
        }
    }

    fun addToFavorites() {
        viewModelScope.launch {
            repository.addCard(_databaseCard.value!!.apply { isFavourite = true })
            _databaseCard.postValue(_databaseCard.value!!)
        }
    }

    fun removeFromFavorites() {
        viewModelScope.launch {
            repository.deleteCard(_databaseCard.value!!.apply { isFavourite = false }.networkId)
            _databaseCard.postValue(_databaseCard.value!!)
        }
    }

}

class CardDetailsViewModelFactory(private val repository: CardRepository, private val cardId: Long) :
    ViewModelProvider
    .Factory {
    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(CardDetailsViewModel::class.java))
            return CardDetailsViewModel(repository, cardId) as T
        throw IllegalArgumentException("unknown view model class")
    }

}