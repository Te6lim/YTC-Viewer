package com.te6lim.ytcviewer.network

import retrofit2.Call
import retrofit2.Retrofit
import retrofit2.converter.scalars.ScalarsConverterFactory
import retrofit2.http.GET

private const val BASE_URL = "https://db.ygoprodeck.com/api/v7/"
private val retrofit = Retrofit.Builder()
    .addConverterFactory(ScalarsConverterFactory.create())
    .baseUrl(BASE_URL)
    .build()

interface YtcApiService {

    @GET("cardinfo.php")
    fun getCardsAsync(): Call<String>
}

object YtcApi {
    val retrofitService: YtcApiService by lazy { retrofit.create(YtcApiService::class.java) }
}