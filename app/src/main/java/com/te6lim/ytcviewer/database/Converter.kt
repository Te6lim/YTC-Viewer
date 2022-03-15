package com.te6lim.ytcviewer.database

import androidx.room.ProvidedTypeConverter
import androidx.room.TypeConverter
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import com.te6lim.ytcviewer.network.CardImage
import com.te6lim.ytcviewer.network.CardPrice
import com.te6lim.ytcviewer.network.CardSet

@ProvidedTypeConverter
class Converter {

    @TypeConverter
    fun List<CardImage?>?.toJsonString(): String {
        return Gson().toJson(this)
    }

    @TypeConverter
    fun String?.toCardImageList(): List<CardImage?>? {
        return Gson().fromJson(this, object : TypeToken<List<CardImage?>?>() {}.type)
    }

    @JvmName("toJsonStringCardSet")
    @TypeConverter
    fun List<CardSet?>?.toJsonString(): String {
        return Gson().toJson(this)
    }

    @TypeConverter
    fun String?.toCardSetList(): List<CardSet?>? {
        return Gson().fromJson(this, object : TypeToken<List<CardSet?>?>() {}.type)
    }

    @JvmName("toJsonStringCardPrice")
    @TypeConverter
    fun List<CardPrice?>?.toJsonString(): String {
        return Gson().toJson(this)
    }

    @TypeConverter
    fun String?.toCardPriceList(): List<CardPrice?>? {
        return Gson().fromJson(this, object : TypeToken<List<CardPrice?>?>() {}.type)
    }
}