package com.te6lim.ytcviewer.home.cards

import androidx.lifecycle.*
import androidx.paging.PagingData
import androidx.paging.insertFooterItem
import androidx.paging.map
import com.te6lim.ytcviewer.database.Card
import com.te6lim.ytcviewer.database.CardDatabase
import com.te6lim.ytcviewer.database.toUiItem
import com.te6lim.ytcviewer.filters.CardFilter
import com.te6lim.ytcviewer.filters.CardFilterCategory
import com.te6lim.ytcviewer.home.SortItem
import com.te6lim.ytcviewer.home.cards.LiveDataSubscriber.Companion.liveDataSubscriber
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

    private val _selectedChips = MutableLiveData<Map<String, Boolean>>()
    val selectedChips: LiveData<Map<String, Boolean>>
        get() = _selectedChips

    private val _selectedCardFilters = MutableLiveData<CardsFlowSource>()

    /*val selectedCardFilters = Transformations.map(_selectedCardFilters) {
        getCardPagingDataFlow(filters = it, sortType = getSortType())
    }*/

    private val _searchKey = MutableLiveData<CardsFlowSource>()
    /*val searchKey = Transformations.map(_searchKey) {
        getCardPagingDataFlow(searchKey = it, sortType = getSortType())
    }*/

    private val _sortType = MutableLiveData<CardsFlowSource>(
        CardsFlowSource.SortType(SortItem.defaultSortType)
    )
    /*val sortType = Transformations.map(_sortType) {
        getCardPagingDataFlow(sortType = (it as CardsFlowSource.SortType).type)
    }*/

    private val _cardSourceType = MutableLiveData(CardSourceTypes.SORT_TYPE)

    val cards = Transformations.switchMap(_cardSourceType) {
        getLiveDataSubscriber(it).subscribedLiveData
    }

    private var cardListType = CardType.MonsterCard

    private val _selectedCard = MutableLiveData<Card?>()
    val selectedCard: LiveData<Card?>
        get() = _selectedCard

    private val repo = CardRepository(db, object : CardRepository.RepoCallback {
        override fun getCardResponseType(): CardType = cardListType
        override fun selectedCategories(): Map<String, Boolean> = selectedChipsCopy()
        override fun sortType(): SortItem = getSortType()
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

    private fun getLiveDataSubscriber(
        cardSourceTypes: CardSourceTypes
    ): LiveDataSubscriber<CardsFlowSource, Flow<PagingData<UiItem>>> {
        when (cardSourceTypes) {
            CardSourceTypes.FILTERING -> {
                return liveDataSubscriber(_selectedCardFilters) {
                    getCardPagingDataFlow(
                        filters = (it as CardsFlowSource.Filters).filters!!, sortType = getSortType()
                    )
                }
            }

            CardSourceTypes.SEARCHING -> {
                return liveDataSubscriber(_searchKey) {
                    getCardPagingDataFlow(
                        searchKey = (it as CardsFlowSource.SearchKey).key!!, sortType = getSortType()
                    )
                }
            }

            CardSourceTypes.SORT_TYPE -> {
                return liveDataSubscriber(_sortType) {
                    getCardPagingDataFlow(
                        sortType = (it as CardsFlowSource.SortType).type
                    )
                }
            }
        }
    }

    fun setCardSourceType(type: CardSourceTypes) {
        if (_cardSourceType.value != type) _cardSourceType.value = type
    }

    private fun getCardPagingDataFlow(
        filters: Map<CardFilterCategory, List<CardFilter>> = mapOf(),
        searchKey: String = "",
        sortType: SortItem
    ) = repo.getCardStream(filters, searchKey, sortType).map { pagingData ->
        pagingData.map { card -> card.toUiItem() }
    }.map { it.insertFooterItem(item = UiItem.Footer) }

    private fun selectedChipsCopy(): MutableMap<String, Boolean> {
        val chips = mutableMapOf<String, Boolean>()
        for (k in selectedChips.value!!.keys) chips[k] = selectedChips.value!![k]!!
        return chips
    }

    fun addFiltersToSelected(category: CardFilterCategory, list: List<CardFilter>) {
        val map =
            (_selectedCardFilters.value as CardsFlowSource.Filters).filters?.toMutableMap() ?: mutableMapOf()
        if (!map.contains(category)) map[category] = list
        else map.replace(category, list)
        setCardSourceType(CardSourceTypes.FILTERING)
        _selectedCardFilters.value = CardsFlowSource.Filters(map)
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
        updateFilters()?.let { _selectedCardFilters.value = CardsFlowSource.Filters(it) }
        return map[chipName]!!
    }

    private fun updateFilters(): Map<CardFilterCategory, List<CardFilter>>? {
        val selected = (_selectedCardFilters.value as CardsFlowSource.Filters).filters?.toMutableMap()
        selected?.let {
            for (k in selectedChips.value!!.keys) {
                if (!selectedChips.value!![k]!!) it.remove(CardFilterCategory.get(k))
            }
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
                }
                categories[chipName] = switch
                cardListType = CardType.NonMonsterCard
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
        updateFilters()?.let { _selectedCardFilters.value = CardsFlowSource.Filters(it) }
    }

    fun setSortType(value: SortItem) {
        setCardSourceType(CardSourceTypes.SORT_TYPE)
        if ((_sortType.value as CardsFlowSource.SortType).type.name != value.name)
            _sortType.value = CardsFlowSource.SortType(value)
    }

    fun getSortType() = (_sortType.value as CardsFlowSource.SortType).type

    fun setSearchKey(key: String) {
        setCardSourceType(CardSourceTypes.SEARCHING)
        if (key != (_searchKey.value as CardsFlowSource.SearchKey).key)
            _searchKey.value = CardsFlowSource.SearchKey(key)
    }

    fun setSelectedCard(card: Card?) {
        _selectedCard.value = card
    }

    fun resetLoadCount() {
        repo.resetLoadCount()
    }
}

sealed class CardsFlowSource {
    class Filters(val filters: Map<CardFilterCategory, List<CardFilter>>?) : CardsFlowSource()
    class SearchKey(val key: String?) : CardsFlowSource()
    class SortType(val type: SortItem) : CardsFlowSource()
}

class CardsViewModelFactory(private val db: CardDatabase) : ViewModelProvider.Factory {

    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(CardsViewModel::class.java)) {
            return CardsViewModel(db) as T
        } else throw IllegalArgumentException()
    }
}