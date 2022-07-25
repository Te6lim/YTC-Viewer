package com.te6lim.ytcviewer.cardList

import androidx.lifecycle.*
import androidx.paging.PagingData
import androidx.paging.cachedIn
import androidx.paging.insertFooterItem
import androidx.paging.map
import com.te6lim.ytcviewer.cardFilters.CardFilter
import com.te6lim.ytcviewer.database.Card
import com.te6lim.ytcviewer.database.toUiItem
import com.te6lim.ytcviewer.model.SortType
import com.te6lim.ytcviewer.model.UiItem
import com.te6lim.ytcviewer.repository.CardRepository
import com.te6lim.ytcviewer.resources.CardFilterCategory
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

class CardsViewModel(private val repository: CardRepository) : ViewModel() {

    enum class CardType {
        MonsterCard, NonMonsterCard
    }

    enum class LoadTrigger {
        FILTER, SEARCH, SORT
    }

    var cardFilterIsVisible = false


    private val _categories = MutableLiveData<Map<String, Boolean>>()
    val categories: LiveData<Map<String, Boolean>>
        get() = _categories

    private val _selectedCardFilters = MutableLiveData<Map<CardFilterCategory, List<CardFilter>>>()

    private val _searchKey = MutableLiveData<String>()

    private val _sortType = MutableLiveData(SortType.defaultSortType)

    private val cardLoadTrigger = MutableLiveData(LoadTrigger.SORT)

    val cards = Transformations.switchMap(cardLoadTrigger) {
        liveDataSubscription(it)
    }

    private var cardListType = CardType.MonsterCard

    private val _selectedCard = MutableLiveData<Card?>()
    val selectedCard: LiveData<Card?>
        get() = _selectedCard

    init {
        val map = mutableMapOf<String, Boolean>()
        for (category in CardFilterCategory.values()) {
            map[category.name] = false
        }
        _categories.value = map
    }

    private fun liveDataSubscription(loadTrigger: LoadTrigger): LiveData<Flow<PagingData<UiItem>>> {
        when (loadTrigger) {
            LoadTrigger.FILTER -> {
                return getCardsLiveData(_selectedCardFilters) {
                    getCardPagingDataFlow(filters = it, sortType = _sortType.value!!)
                }
            }

            LoadTrigger.SEARCH -> {
                return getCardsLiveData(_searchKey) {
                    getCardPagingDataFlow(searchKey = it, sortType = _sortType.value!!)
                }
            }
            else -> {
                return getCardsLiveData(_sortType) { sortItem ->
                    _selectedCardFilters.value?.let { filters ->
                        getCardPagingDataFlow(filters = filters, sortType = sortItem)
                    } ?: getCardPagingDataFlow(searchKey = _searchKey.value ?: "", sortType = sortItem)
                }
            }
        }
    }

    private fun <T, R> getCardsLiveData(liveData: LiveData<T>, func: (data: T) -> R): LiveData<R> {
        return Transformations.map(liveData) {
            func(it)
        }
    }

    private fun setLoadTrigger(type: LoadTrigger) {
        if (cardLoadTrigger.value != type) cardLoadTrigger.value = type
    }

    private fun getCardPagingDataFlow(
        filters: Map<CardFilterCategory, List<CardFilter>> = mapOf(),
        searchKey: String = "",
        sortType: SortType
    ) = repository.getCardStream(filters, cardListType, searchKey, sortType)
        .map { pagingData ->
            pagingData.map { card -> card.toUiItem() }
        }.map { it.insertFooterItem(item = UiItem.Footer) }.cachedIn(viewModelScope)

    fun addFiltersToSelected(category: CardFilterCategory, list: List<CardFilter>) {
        val map = _selectedCardFilters.value?.toMutableMap() ?: mutableMapOf()
        if (!map.contains(category)) map[category] = list
        else map.replace(category, list)
        setLoadTrigger(LoadTrigger.FILTER)
        _selectedCardFilters.value = map
    }

    fun toggleCategory(name: String): Boolean {
        val map = when (name) {
            CardFilterCategory.Spell.name -> {
                cardListType = CardType.NonMonsterCard
                toggleSpellOrTrapCategory(name)
            }

            CardFilterCategory.Trap.name -> {
                cardListType = CardType.NonMonsterCard
                toggleSpellOrTrapCategory(name)
            }

            else -> {
                cardListType = CardType.MonsterCard
                toggleNonSpellAndTrapCategory(name)
            }
        }
        _categories.value = map
        updateFilters()?.let { _selectedCardFilters.value = it }
        return map[name]!!
    }

    private fun updateFilters(): Map<CardFilterCategory, List<CardFilter>>? {
        val selected = _selectedCardFilters.value?.toMutableMap()
        selected?.let {
            for (k in categories.value!!.keys) {
                if (!categories.value!![k]!!) it.remove(CardFilterCategory.get(k))
            }
            if (it.isEmpty()) cardLoadTrigger.value = LoadTrigger.SORT
        }
        return selected
    }

    private fun toggleNonSpellAndTrapCategory(name: String): Map<String, Boolean> {
        val categories = _categories.value!!.toMutableMap()

        if (categories[CardFilterCategory.Spell.name]!!) categories[CardFilterCategory.Spell.name] = false

        if (categories[CardFilterCategory.Trap.name]!!) categories[CardFilterCategory.Trap.name] = false

        categories[name] = !categories[name]!!

        return categories

    }

    private fun toggleSpellOrTrapCategory(name: String): Map<String, Boolean> {
        val categories = _categories.value!!.toMutableMap()
        if (!categories[name]!!) {
            for (s in categories.keys) {
                categories[s] = s == name
            }
            categories[name] = true
        } else {
            categories[name] = false
        }

        return categories
    }

    fun switchChip(chipName: String, switch: Boolean) {
        val categories = _categories.value!!.toMutableMap()

        when (chipName) {
            CardFilterCategory.Spell.name, CardFilterCategory.Trap.name -> {
                if (switch) {
                    for (s in categories.keys) categories[s] = s == chipName
                    cardListType = CardType.NonMonsterCard
                }
                categories[chipName] = switch
            }

            else -> {
                if (categories[CardFilterCategory.Spell.name]!!) {
                    categories[CardFilterCategory.Spell.name] = false
                }

                if (categories[CardFilterCategory.Trap.name]!!) {
                    categories[CardFilterCategory.Trap.name] = false
                }

                categories[chipName] = switch
                cardListType = CardType.MonsterCard
            }
        }
        _categories.value = categories
        updateFilters()?.let { _selectedCardFilters.value = it }
    }

    fun deselectAllSelectedCategories() {
        val categories = categories.value?.toMutableMap()
        categories?.keys?.forEach {
            switchChip(it, false)
        }
    }

    fun setSortType(value: SortType) {
        if (_sortType.value!!.name != value.name) _sortType.value = value
        setLoadTrigger(LoadTrigger.SORT)
    }

    fun getSortType() = _sortType.value ?: SortType.defaultSortType

    fun setSearchKey(key: String) {
        setLoadTrigger(LoadTrigger.SEARCH)
        _searchKey.value?.let {
            if (key != it) _searchKey.value = key
        } ?: run { _searchKey.value = key }
    }

    fun setSelectedCard(card: Card?) {
        _selectedCard.value = card
    }
}

class CardsViewModelFactory(private val repository: CardRepository) : ViewModelProvider.Factory {

    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(CardsViewModel::class.java)) {
            return CardsViewModel(repository) as T
        } else throw IllegalArgumentException()
    }
}