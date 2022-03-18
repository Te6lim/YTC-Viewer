package com.te6lim.ytcviewer.home.cards

import androidx.lifecycle.*
import com.te6lim.ytcviewer.database.CardDatabase
import com.te6lim.ytcviewer.domain.DomainCard
import com.te6lim.ytcviewer.filters.CardFilterCategory
import com.te6lim.ytcviewer.network.NetworkStatus
import com.te6lim.ytcviewer.repository.CardRepository
import com.te6lim.ytcviewer.repository.ConnectivityResolver
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

    private val _selectedCategories =
        MutableLiveData<Map<String, CardFilterCategory>>(mutableMapOf())

    val selectedCategories: LiveData<Map<String, CardFilterCategory>>
        get() = _selectedCategories

    var lastChecked: String? = null
        private set

    private var selectedTypeFilters = arrayOf<String>()

    private var selectedRaceFilters = arrayOf<String>()

    private var selectedAttributeFilters = arrayOf<String>()

    private val repository = CardRepository(cardDb, _networkStatus, object : ConnectivityResolver {

        override fun selectCategoryOnOfflineLoad(category: String) {
            addToSelectedCategories(category)
        }

        override fun hasSelectedCategory() = selectedCategories.value?.isNotEmpty() ?: false

    })

    private val _cards = repository.resolveCardListSource()
    val cards: LiveData<List<DomainCard>?> get() = _cards

    var lastSearchQuery: String? = null
        private set

    fun getCards() {
        lastSearchQuery = null
        _cards.value = null
        repository.getCardStream(selectedFilters.value!!, lastChecked!!)
    }

    fun getCardsWithSearch(value: String) {
        lastSearchQuery = value
        _cards.value = null
        repository.getCardsWithSearch(value)
    }

    fun addToSelectedCategories(category: String) {
        val map = _selectedCategories.value!!.toMutableMap()
        map[category] = CardFilterCategory.valueOf(category)
        lastChecked = category
        _selectedCategories.value = map
    }

    fun removeCategoryFromSelected(category: String) {
        val map = _selectedCategories.value!!.toMutableMap()
        map.remove(category)

        when (CardFilterCategory.valueOf(category)) {
            CardFilterCategory.Type -> selectedTypeFilters = arrayOf()

            CardFilterCategory.Race -> selectedRaceFilters = arrayOf()

            CardFilterCategory.Attribute -> selectedAttributeFilters = arrayOf()

            CardFilterCategory.Spell -> selectedRaceFilters = arrayOf()

            CardFilterCategory.Trap -> selectedRaceFilters = arrayOf()
        }

        _selectedCategories.value = map

        if (map.isEmpty()) _cards.value = null

        removeFilter(CardFilterCategory.valueOf(category).query)
    }

    fun removeAllSelectedCategory() {
        selectedTypeFilters = arrayOf()
        selectedRaceFilters = arrayOf()
        selectedAttributeFilters = arrayOf()
        _selectedCategories.value?.forEach {
            removeFilter(CardFilterCategory.valueOf(it.key).query)
        }
        _selectedCategories.value = mutableMapOf()
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
        selectedFilters.value?.let {
            if (it.isNotEmpty()) selectedFilters.value = it.apply { remove(key) }
        }
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