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

    private val _checkedMonsterCategories =
        MutableLiveData<Map<String, FilterSelectionViewModel.CardFilterCategory>>(
            mutableMapOf()
        )
    val checkedMonsterCategories: LiveData<Map<String, FilterSelectionViewModel.CardFilterCategory>>
        get() = _checkedMonsterCategories

    private val _checkedNonMonsterCategories =
        MutableLiveData<Map<String, FilterSelectionViewModel.NonMonsterCardFilterCategory>>(
            mutableMapOf()
        )

    val checkedNonMonsterCategories: LiveData<Map<String, FilterSelectionViewModel.NonMonsterCardFilterCategory>>
        get() = _checkedNonMonsterCategories

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

    fun addMonsterCategoryToChecked(category: String) {
        val map = _checkedMonsterCategories.value!!.toMutableMap()
        map[category] = FilterSelectionViewModel.CardFilterCategory.valueOf(category)
        _checkedMonsterCategories.value = map
    }

    fun removeMonsterCategoryFromChecked(category: String) {
        val map = _checkedMonsterCategories.value!!.toMutableMap()
        map.remove(category)
        _checkedMonsterCategories.value = map
    }

    fun addNonMonsterCategoryToChecked(category: String) {
        val map = _checkedNonMonsterCategories.value!!.toMutableMap()
        map[category] = FilterSelectionViewModel.NonMonsterCardFilterCategory.valueOf(category)
        _checkedNonMonsterCategories.value = map
    }

    fun removeNonMonsterCategoryFromChecked(category: String) {
        val map = _checkedNonMonsterCategories.value!!.toMutableMap()
        map.remove(category)
        _checkedNonMonsterCategories.value = map
    }

    fun removeAllCheckedMonsterCategory() {
        _checkedMonsterCategories.value = mutableMapOf()
    }

    fun removeAllCheckedNonMonsterCategory() {
        _checkedNonMonsterCategories.value = mutableMapOf()
    }
}