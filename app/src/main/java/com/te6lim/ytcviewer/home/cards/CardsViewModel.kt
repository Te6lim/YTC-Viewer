package com.te6lim.ytcviewer.home.cards

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.te6lim.ytcviewer.network.YtcApi
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class CardsViewModel : ViewModel() {

    private val _propertiesString = MutableLiveData<String>()
    val propertiesString: LiveData<String>
        get() = _propertiesString

    private val _checkedCategories =
        MutableLiveData<Map<String, FilterSelectionViewModel.CardFilterCategory>>(
            mutableMapOf()
        )
    val checkedCategories: LiveData<Map<String, FilterSelectionViewModel.CardFilterCategory>>
        get() = _checkedCategories

    private val _selectedFilter = MutableLiveData<String?>(null)
    val selectedFilter: LiveData<String?>
        get() = _selectedFilter

    init {
        //getProperties()
    }

    private fun getProperties() {
        YtcApi.retrofitService.getCardsAsync().enqueue(object : Callback<String> {
            override fun onResponse(call: Call<String>, response: Response<String>) {
                _propertiesString.value = "success"
            }

            override fun onFailure(call: Call<String>, t: Throwable) {
                _propertiesString.value = t.message
            }
        })

        /*try {
            _propertiesString.value = string.await()

        } catch (t: Throwable) {
            _propertiesString.value = t.message
        }*/
    }

    fun addCategoryToChecked(category: String) {
        val map = _checkedCategories.value!!.toMutableMap()
        map[category] = FilterSelectionViewModel.CardFilterCategory.valueOf(category)
        _checkedCategories.value = map
    }

    fun removeCategoryFromChecked(category: String) {
        val map = _checkedCategories.value!!.toMutableMap()
        map.remove(category)

        _checkedCategories.value = map
    }

    fun removeAllCheckedCategory() {
        _checkedCategories.value = mutableMapOf()
    }

    fun setSelectedFilter(value: String?) {
        _selectedFilter.value = value
    }
}