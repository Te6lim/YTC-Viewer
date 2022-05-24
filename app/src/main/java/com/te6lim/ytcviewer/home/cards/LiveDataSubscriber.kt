package com.te6lim.ytcviewer.home.cards

import androidx.lifecycle.LiveData
import androidx.lifecycle.Transformations
import kotlin.reflect.KType

class LiveDataSubscriber<T, R> private constructor(
    private val liveData: LiveData<T>, func: (data: T) -> R
) {

    companion object {

        fun <T, R> liveDataSubscriber(
            liveData: LiveData<T>, func: (data: T) -> R
        ): LiveDataSubscriber<T, R> {
            return LiveDataSubscriber(liveData) { func(liveData.value!!) }
        }
    }

    val subscribedLiveData = Transformations.map(liveData) {
        func(it)
    }

    fun getSourceType(): KType {
        val source = this::liveData
        return source.returnType
    }
}