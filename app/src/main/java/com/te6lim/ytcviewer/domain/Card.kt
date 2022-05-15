package com.te6lim.ytcviewer.domain

import com.google.gson.annotations.SerializedName
import com.te6lim.ytcviewer.network.CardImage
import com.te6lim.ytcviewer.network.CardPrice
import com.te6lim.ytcviewer.network.CardSet

class DomainCard(
    val id: Long,
    val networkId: Long,
    val name: String,
    val type: String?,
    val desc: String?,
    val race: String?,
    val atk: Int?,
    val def: Int?,
    val level: Int?,
    val attribute: String?,
    val archetype: String?,
    @SerializedName("card_sets") val cardSets: List<CardSet?>?,
    @SerializedName("card_images") val cardImages: List<CardImage?>?,
    @SerializedName("card_prices") val cardPrices: List<CardPrice?>?
)