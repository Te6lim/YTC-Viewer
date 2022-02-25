package com.te6lim.ytcviewer.network

import retrofit2.Call
import retrofit2.Retrofit
import retrofit2.converter.scalars.ScalarsConverterFactory
import retrofit2.http.GET
import retrofit2.http.QueryMap

private const val BASE_URL = "https://db.ygoprodeck.com/api/v7/"
private val retrofit = Retrofit.Builder()
    .addConverterFactory(ScalarsConverterFactory.create())
    .baseUrl(BASE_URL)
    .build()

interface YtcApiService {

    @GET("cardinfo.php")
    fun getCardsAsync(
        @QueryMap query: Map<String, Array<String>>
    ): Call<String>

    @GET("cardinfo.php")
    fun getCardsAsync(
        @QueryMap query1: Map<String, Array<String>>,
        @QueryMap query2: Map<String, Array<String>>
    ): Call<String>

    @GET("cardinfo.php")
    fun getCardsAsync(
        @QueryMap query1: Map<String, Array<String>>,
        @QueryMap query2: Map<String, Array<String>>,
        @QueryMap query3: Map<String, Array<String>>
    ): Call<String>
}

object YtcApi {
    val retrofitService: YtcApiService by lazy { retrofit.create(YtcApiService::class.java) }
}