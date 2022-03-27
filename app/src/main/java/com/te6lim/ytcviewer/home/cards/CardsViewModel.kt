package com.te6lim.ytcviewer.home.cards

import androidx.lifecycle.*
import androidx.paging.ExperimentalPagingApi
import androidx.paging.PagingData
import androidx.paging.cachedIn
import androidx.paging.map
import com.te6lim.ytcviewer.database.CardDatabase
import com.te6lim.ytcviewer.database.toDomainCard
import com.te6lim.ytcviewer.domain.DomainCard
import com.te6lim.ytcviewer.filters.CardFilterCategory
import com.te6lim.ytcviewer.network.NetworkStatus
import com.te6lim.ytcviewer.repository.CardRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import kotlin.collections.set

class CardsViewModel(db: CardDatabase) : ViewModel() {

    private val selectedFilters = MutableLiveData<MutableMap<String, Array<String>>>()

    private val searchKey = MutableLiveData<String>()

    private val _networkStatus = MutableLiveData<NetworkStatus>()
    val networkStatus: LiveData<NetworkStatus> get() = _networkStatus

    val cards = resolveCardSearchMethod()

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

    private val repository = CardRepository(db)

    @OptIn(ExperimentalPagingApi::class)
    fun getCards() {
        lastSearchQuery = null
    }

    private fun resolveCardSearchMethod(): LiveData<Flow<PagingData<DomainCard>>> {
        val result = MediatorLiveData<Flow<PagingData<DomainCard>>>()

        val filterObserver = Observer<Map<String, Array<String>>> {
            if (it.isNotEmpty()) lastSearchQuery = null
            result.value = repository.getCardStream(it, lastChecked = lastChecked!!)
                .map { pagingData ->
                    pagingData.map { card ->
                        card.toDomainCard()
                    }
                }.cachedIn(viewModelScope)
        }

        val searchKeyObserver = Observer<String> {
            lastSearchQuery = it
            result.value = repository.getCardStream(searchKey = it)
                .map { pagingData ->
                    pagingData.map { card ->
                        card.toDomainCard()
                    }
                }.cachedIn(viewModelScope)
        }

        result.addSource(selectedFilters, filterObserver)
        result.addSource(searchKey, searchKeyObserver)
        return result
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

    fun setSearchKey(key: String) {
        searchKey.value = key
    }
}

class CardsViewModelFactory(private val db: CardDatabase) :
    ViewModelProvider.Factory {
    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(CardsViewModel::class.java))
            return CardsViewModel(db) as T
        else throw java.lang.IllegalArgumentException("unknown viewModel class")
    }
}