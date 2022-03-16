package com.te6lim.ytcviewer.home.cards

import androidx.lifecycle.*
import com.te6lim.ytcviewer.database.CardDatabase
import com.te6lim.ytcviewer.domain.DomainCard
import com.te6lim.ytcviewer.filters.CardFilterCategory
import com.te6lim.ytcviewer.network.NetworkStatus
import com.te6lim.ytcviewer.repository.CardRepository
import kotlin.collections.set

class CardsViewModel(cardDb: CardDatabase) : ViewModel() {

    private val selectedFilters = MutableLiveData<MutableMap<String, Array<String>>>()

    private val _networkStatus = MutableLiveData<NetworkStatus>()
    val networkStatus: LiveData<NetworkStatus> get() = _networkStatus

    val filterTransformation = Transformations.map(selectedFilters) {
        if (it.isNotEmpty()) {
            lastSearchQuery = null
            _cards.value = null
            repository.getCards(it, lastChecked!!)
        }
    }

    private val _checkedCategories =
        MutableLiveData<Map<String, CardFilterCategory>>(mutableMapOf())

    val checkedCategories: LiveData<Map<String, CardFilterCategory>>
        get() = _checkedCategories

    var lastChecked: String? = null
        private set

    private var selectedTypeFilters = arrayOf<String>()

    private var selectedRaceFilters = arrayOf<String>()

    private var selectedAttributeFilters = arrayOf<String>()

    private val repository = CardRepository(cardDb, _networkStatus)

    private val _cards = repository.resolveCardListSource(this::addCategoryToChecked)
    val cards: LiveData<List<DomainCard>?> get() = _cards

    var lastSearchQuery: String? = null
        private set

    fun getCards() {
        lastSearchQuery = null
        _cards.value = null
        repository.getCards(selectedFilters.value!!, lastChecked!!)
    }

    fun getCardsWithSearch(value: String) {
        lastSearchQuery = value
        _cards.value = null
        repository.getCardsWithSearch(value)
    }

    fun addCategoryToChecked(category: String) {
        val map = _checkedCategories.value!!.toMutableMap()
        map[category] = CardFilterCategory.valueOf(category)
        lastChecked = category
        _checkedCategories.value = map
    }

    fun removeCategoryFromChecked(category: String) {
        val map = _checkedCategories.value!!.toMutableMap()
        map.remove(category)

        when (CardFilterCategory.valueOf(category)) {
            CardFilterCategory.Type -> selectedTypeFilters = arrayOf()

            CardFilterCategory.Race -> selectedRaceFilters = arrayOf()

            CardFilterCategory.Attribute -> selectedAttributeFilters = arrayOf()

            else -> throw IllegalArgumentException()
        }

        _checkedCategories.value = map

        if (map.isEmpty()) _cards.value = null

        removeFilter(CardFilterCategory.valueOf(category).query)
    }

    fun removeAllCheckedCategory() {
        selectedTypeFilters = arrayOf()
        selectedRaceFilters = arrayOf()
        selectedAttributeFilters = arrayOf()
        _checkedCategories.value?.forEach {
            removeFilter(CardFilterCategory.valueOf(it.key).query)
        }
        _checkedCategories.value = mutableMapOf()
        _cards.value = null

    }

    fun setSelectedFilter(filters: List<String>) {
        lastChecked?.let {
            when (CardFilterCategory.valueOf(it)) {
                CardFilterCategory.Type -> {
                    selectedTypeFilters = filters.toTypedArray()
                }

                CardFilterCategory.Race -> {
                    selectedRaceFilters = filters.toTypedArray()
                }

                CardFilterCategory.Attribute -> {
                    selectedAttributeFilters = filters.toTypedArray()
                }

                CardFilterCategory.Spell -> {
                    selectedRaceFilters = filters.toTypedArray()
                }

                CardFilterCategory.Trap -> {
                    selectedRaceFilters = filters.toTypedArray()
                }
            }

            val map = mutableMapOf<String, Array<String>>()
            if (selectedTypeFilters.isNotEmpty())
                map[CardFilterCategory.Type.query] = selectedTypeFilters

            if (selectedRaceFilters.isNotEmpty())
                map[CardFilterCategory.Race.query] = selectedRaceFilters

            if (selectedAttributeFilters.isNotEmpty())
                map[CardFilterCategory.Attribute.query] = selectedAttributeFilters

            selectedFilters.value = map
        }
    }

    private fun removeFilter(key: String) {
        selectedFilters.value = selectedFilters.value!!.apply { remove(key) }
    }
}

class CardsViewModelFactory(private val cardDb: CardDatabase) : ViewModelProvider.Factory {
    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(CardsViewModel::class.java))
            return CardsViewModel(cardDb) as T
        else throw java.lang.IllegalArgumentException("unknown viewModel class")
    }
}