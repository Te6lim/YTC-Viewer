package com.te6lim.ytcviewer.network

import com.jakewharton.retrofit2.adapter.kotlin.coroutines.CoroutineCallAdapterFactory
import com.squareup.moshi.Moshi
import com.squareup.moshi.kotlin.reflect.KotlinJsonAdapterFactory
import kotlinx.coroutines.Deferred
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory
import retrofit2.http.GET
import retrofit2.http.Query
import retrofit2.http.QueryMap

enum class NetworkStatus {
    LOADING, ERROR, DONE
}

private const val BASE_URL = "https://db.ygoprodeck.com/api/v7/"

private val interceptor = HttpLoggingInterceptor().apply {
    level = HttpLoggingInterceptor.Level.BODY
}

private val httpClient = OkHttpClient.Builder().addInterceptor(interceptor).build()

private val moshi = Moshi.Builder().add(KotlinJsonAdapterFactory()).build()

private val retrofit = Retrofit.Builder()
    .addConverterFactory(MoshiConverterFactory.create(moshi))
    .addCallAdapterFactory(CoroutineCallAdapterFactory())
    .client(httpClient)
    .baseUrl(BASE_URL)
    .build()

const val PAGE_SIZE = 9

interface YtcApiService {

    @GET("cardinfo.php")
    fun getCardsAsync(
        @QueryMap query: Map<String, String>,
        @Query("fname") searchString: String = "",
        @Query("num") pageSize: Int = PAGE_SIZE,
        @Query("offset") offset: Int,
        @Query("sort") sort: String
    ): Deferred<Response>

    @GET("cardinfo.php")
    fun getCardsAsync(
        @QueryMap query1: Map<String, String>,
        @QueryMap query2: Map<String, String>,
        @Query("fname") searchString: String = "",
        @Query("num") pageSize: Int = PAGE_SIZE,
        @Query("offset") offset: Int,
        @Query("sort") sort: String
    ): Deferred<Response>

    @GET("cardinfo.php")
    fun getCardsAsync(
        @QueryMap query1: Map<String, String>,
        @QueryMap query2: Map<String, String>,
        @QueryMap query3: Map<String, String>,
        @Query("fname") searchString: String = "",
        @Query("num") pageSize: Int = PAGE_SIZE,
        @Query("offset") offset: Int,
        @Query("sort") sort: String
    ): Deferred<Response>

    @GET("cardinfo.php")
    fun getCardsAsync(
        @QueryMap query1: Map<String, String>,
        @QueryMap query2: Map<String, String>,
        @QueryMap query3: Map<String, String>,
        @QueryMap query4: Map<String, String>,
        @Query("fname") searchString: String = "",
        @Query("num") pageSize: Int = PAGE_SIZE,
        @Query("offset") offset: Int,
        @Query("sort") sort: String
    ): Deferred<Response>


    @GET("cardinfo.php")
    fun getCardsBySearchAsync(
        @Query("fname") searchString: String,
        @Query("num") pageSize: Int = PAGE_SIZE,
        @Query("offset") offset: Int,
        @Query("sort") sort: String
    ): Deferred<Response>
}

object YtcApi {
    val retrofitService: YtcApiService by lazy { retrofit.create(YtcApiService::class.java) }
}