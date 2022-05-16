package com.te6lim.ytcviewer.details

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.te6lim.ytcviewer.domain.Card

class CardDetailsViewModel(val card: Card) : ViewModel() {
    private val liveDataCard: LiveData<Card>
        get() = MutableLiveData(card)
}

class CardDetailsViewModelFactory(private val card: Card) : ViewModelProvider.Factory {
    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(CardDetailsViewModel::class.java))
            return CardDetailsViewModel(card) as T
        throw IllegalArgumentException("unknown view model class")
    }

}