package com.te6lim.ytcviewer.home.cards

import androidx.lifecycle.*
import androidx.paging.PagingData
import androidx.paging.cachedIn
import androidx.paging.insertFooterItem
import androidx.paging.map
import com.te6lim.ytcviewer.database.Card
import com.te6lim.ytcviewer.database.CardDatabase
import com.te6lim.ytcviewer.database.toUiItem
import com.te6lim.ytcviewer.filters.CardFilter
import com.te6lim.ytcviewer.filters.CardFilterCategory
import com.te6lim.ytcviewer.home.SortItem
import com.te6lim.ytcviewer.repository.CardRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

class CardsViewModel(db: CardDatabase) : ViewModel() {

    enum class CardType {
        MonsterCard, NonMonsterCard
    }

    enum class CardSourceTypes {
        FILTERING, SEARCHING, SORT_TYPE
    }

    var cardFilterIsVisible = false


    private val _selectedChips = MutableLiveData<Map<String, Boolean>>()
    val selectedChips: LiveData<Map<String, Boolean>>
        get() = _selectedChips

    private val _selectedCardFilters = MutableLiveData<Map<CardFilterCategory, List<CardFilter>>>()

    private val _searchKey = MutableLiveData<String>()

    private val _sortType = MutableLiveData(SortItem.defaultSortType)

    private val _cardSourceType = MutableLiveData(CardSourceTypes.SORT_TYPE)

    val cards = Transformations.switchMap(_cardSourceType) {
        liveDataSubscription(it)
    }

    private var cardListType = CardType.MonsterCard

    private val _selectedCard = MutableLiveData<Card?>()
    val selectedCard: LiveData<Card?>
        get() = _selectedCard

    private val repo = CardRepository(db, object : CardRepository.RepoCallback {
        override fun getCardResponseType(): CardType = cardListType
        override fun selectedCategories(): Map<String, Boolean> = selectedChipsCopy()
        override fun sortType(): SortItem = _sortType.value!!
    })

    val isPagingDataEmpty = repo.isEmpty

    val connectionStatus = repo.connectionStatus

    init {
        val map = mutableMapOf<String, Boolean>()
        for (category in CardFilterCategory.values()) {
            map[category.name] = false
        }
        _selectedChips.value = map
    }

    private fun liveDataSubscription(cardSourceTypes: CardSourceTypes): LiveData<Flow<PagingData<UiItem>>> {
        when (cardSourceTypes) {
            CardSourceTypes.FILTERING -> {
                return getCardsLiveData(_selectedCardFilters) {
                    getCardPagingDataFlow(filters = it, sortType = _sortType.value!!)
                }
            }

            CardSourceTypes.SEARCHING -> {
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

    private fun setCardSourceType(type: CardSourceTypes) {
        if (_cardSourceType.value != type) _cardSourceType.value = type
    }

    private fun getCardPagingDataFlow(
        filters: Map<CardFilterCategory, List<CardFilter>> = mapOf(),
        searchKey: String = "",
        sortType: SortItem
    ) = repo.getCardStream(filters, searchKey, sortType).map { pagingData ->
        pagingData.map { card -> card.toUiItem() }
    }.map { it.insertFooterItem(item = UiItem.Footer) }.cachedIn(viewModelScope)

    private fun selectedChipsCopy(): MutableMap<String, Boolean> {
        val chips = mutableMapOf<String, Boolean>()
        for (k in selectedChips.value!!.keys) chips[k] = selectedChips.value!![k]!!
        return chips
    }

    fun addFiltersToSelected(category: CardFilterCategory, list: List<CardFilter>) {
        val map = _selectedCardFilters.value?.toMutableMap() ?: mutableMapOf()
        if (!map.contains(category)) map[category] = list
        else map.replace(category, list)
        setCardSourceType(CardSourceTypes.FILTERING)
        _selectedCardFilters.value = map
    }

    fun toggleChip(chipName: String): Boolean {
        val map = when (chipName) {
            CardFilterCategory.Spell.name -> {
                cardListType = CardType.NonMonsterCard
                toggleSpellOrTrap(chipName)
            }

            CardFilterCategory.Trap.name -> {
                cardListType = CardType.NonMonsterCard
                toggleSpellOrTrap(chipName)
            }

            else -> {
                cardListType = CardType.MonsterCard
                toggleNonSpellAndTrap(chipName)
            }
        }
        _selectedChips.value = map
        updateFilters()?.let { _selectedCardFilters.value = it }
        return map[chipName]!!
    }

    private fun updateFilters(): Map<CardFilterCategory, List<CardFilter>>? {
        val selected = _selectedCardFilters.value?.toMutableMap()
        selected?.let {
            for (k in selectedChips.value!!.keys) {
                if (!selectedChips.value!![k]!!) it.remove(CardFilterCategory.get(k))
            }
            if (it.isEmpty()) _cardSourceType.value = CardSourceTypes.SORT_TYPE
        }
        return selected
    }

    private fun toggleNonSpellAndTrap(chipName: String): Map<String, Boolean> {
        val categories = _selectedChips.value!!.toMutableMap()

        if (categories[CardFilterCategory.Spell.name]!!) categories[CardFilterCategory.Spell.name] = false

        if (categories[CardFilterCategory.Trap.name]!!) categories[CardFilterCategory.Trap.name] = false

        categories[chipName] = !categories[chipName]!!

        return categories

    }

    private fun toggleSpellOrTrap(chipName: String): Map<String, Boolean> {
        val categories = _selectedChips.value!!.toMutableMap()
        if (!categories[chipName]!!) {
            for (s in categories.keys) {
                categories[s] = s == chipName
            }
            categories[chipName] = true
        } else {
            categories[chipName] = false
        }

        return categories
    }

    fun switchChip(chipName: String, switch: Boolean) {
        val categories = _selectedChips.value!!.toMutableMap()

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
        _selectedChips.value = categories
        updateFilters()?.let { _selectedCardFilters.value = it }
    }

    fun deselectAllSelectedCategories() {
        val categories = selectedChips.value?.toMutableMap()
        categories?.keys?.forEach {
            switchChip(it, false)
        }
    }

    fun setSortType(value: SortItem) {
        if (_sortType.value!!.name != value.name) _sortType.value = value
        setCardSourceType(CardSourceTypes.SORT_TYPE)
    }

    fun getSortType() = _sortType.value ?: SortItem.defaultSortType

    fun setSearchKey(key: String) {
        setCardSourceType(CardSourceTypes.SEARCHING)
        _searchKey.value?.let {
            if (key != it) _searchKey.value = key
        } ?: run { _searchKey.value = key }
    }

    fun setSelectedCard(card: Card?) {
        _selectedCard.value = card
    }

    fun resetLoadCount() {
        repo.resetLoadCount()
    }

    private fun <T, R> getCardsLiveData(liveData: LiveData<T>, func: (data: T) -> R): LiveData<R> {
        return Transformations.map(liveData) {
            func(it)
        }
    }
}

class CardsViewModelFactory(private val db: CardDatabase) : ViewModelProvider.Factory {

    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(CardsViewModel::class.java)) {
            return CardsViewModel(db) as T
        } else throw IllegalArgumentException()
    }
}