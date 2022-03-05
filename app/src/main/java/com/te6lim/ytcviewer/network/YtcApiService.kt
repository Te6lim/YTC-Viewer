package com.te6lim.ytcviewer.network

import com.jakewharton.retrofit2.adapter.kotlin.coroutines.CoroutineCallAdapterFactory
import com.squareup.moshi.Moshi
import com.squareup.moshi.kotlin.reflect.KotlinJsonAdapterFactory
import com.te6lim.ytcviewer.models.Response
import kotlinx.coroutines.Deferred
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory
import retrofit2.http.GET
import retrofit2.http.QueryMap

private const val BASE_URL = "https://db.ygoprodeck.com/api/v7/"


//private val httpClient = OkHttpClient.Builder().addInterceptor(interceptor).build()

private val moshi = Moshi.Builder().add(KotlinJsonAdapterFactory()).build()

private val retrofit = Retrofit.Builder()
    .addConverterFactory(MoshiConverterFactory.create(moshi))
    //.addConverterFactory(ScalarsConverterFactory.create())
    .addCallAdapterFactory(CoroutineCallAdapterFactory())
    .baseUrl(BASE_URL)
    .build()

interface YtcApiService {

    @GET("cardinfo.php")
    fun getCardsAsync(
        @QueryMap query: Map<String, String>
    ): Deferred<Response>

    @GET("cardinfo.php")
    fun getCardsAsync(
        @QueryMap query1: Map<String, String>,
        @QueryMap query2: Map<String, String>
    ): Deferred<Response>

    @GET("cardinfo.php")
    fun getCardsAsync(
        @QueryMap query1: Map<String, String>,
        @QueryMap query2: Map<String, String>,
        @QueryMap query3: Map<String, String>
    ): Deferred<Response>
}

object YtcApi {
    val retrofitService: YtcApiService by lazy { retrofit.create(YtcApiService::class.java) }
}