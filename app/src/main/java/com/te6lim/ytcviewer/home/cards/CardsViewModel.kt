package com.te6lim.ytcviewer.home.cards

import androidx.lifecycle.*
import androidx.paging.ExperimentalPagingApi
import androidx.paging.cachedIn
import androidx.paging.map
import com.te6lim.ytcviewer.database.CardDatabase
import com.te6lim.ytcviewer.database.toDomainCard
import com.te6lim.ytcviewer.filters.CardFilterCategory
import com.te6lim.ytcviewer.network.NetworkStatus
import com.te6lim.ytcviewer.repository.CardRepository
import com.te6lim.ytcviewer.repository.CardType
import kotlinx.coroutines.flow.map
import kotlin.collections.set

class CardsViewModel(db: CardDatabase, type: String?) : ViewModel() {

    private val selectedFilters = MutableLiveData<MutableMap<String, Array<String>>>()

    private val _networkStatus = MutableLiveData<NetworkStatus>()
    val networkStatus: LiveData<NetworkStatus> get() = _networkStatus

    val cards = Transformations.map(selectedFilters) {
        if (it.isNotEmpty()) lastSearchQuery = null
        repository.getCardStream(it, lastChecked!!).map { pagingData ->
            pagingData.map { databaseCard ->
                databaseCard.toDomainCard()
            }
        }.cachedIn(viewModelScope)
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

    var lastSearchQuery: String? = null
        private set

    private val _lastTypeCached = MutableLiveData<CardType?>()
    val lastTypeCached = Transformations.map(_lastTypeCached) {
        it?.name
    }

    init {
        _lastTypeCached.value = type?.let { CardType.valueOf(it) }
    }

    private val repository = CardRepository(db, _lastTypeCached)

    @OptIn(ExperimentalPagingApi::class)
    fun getCards() {
        lastSearchQuery = null
        //_cards = repository.getCardStream(selectedFilters.value!!, lastChecked!!)
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

        //if (map.isEmpty()) _cards = null

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
        //_cards = null

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

class CardsViewModelFactory(private val db: CardDatabase, private val lastTypeCached: String?) :
    ViewModelProvider.Factory {
    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(CardsViewModel::class.java))
            return CardsViewModel(db, lastTypeCached) as T
        else throw java.lang.IllegalArgumentException("unknown viewModel class")
    }
}