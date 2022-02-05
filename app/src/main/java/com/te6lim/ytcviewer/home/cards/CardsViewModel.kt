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
}