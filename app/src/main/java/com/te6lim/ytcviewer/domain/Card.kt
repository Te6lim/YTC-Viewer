package com.te6lim.ytcviewer.domain

import com.google.gson.annotations.SerializedName
import com.te6lim.ytcviewer.network.CardImage
import com.te6lim.ytcviewer.network.CardPrice
import com.te6lim.ytcviewer.network.CardSet

open class DomainCard(
    open val id: Long,
    open val networkId: Long,
    open val name: String,
    open @SerializedName("card_images") val cardImages: List<CardImage?>?
) {

    data class DomainMonsterCard(
        override val id: Long,
        override val networkId: Long,
        override val name: String,
        val type: String,
        val desc: String,
        val race: String,
        val atk: Int? = null,
        val def: Int? = null,
        val level: Int? = null,
        val attribute: String,
        @SerializedName("card_sets") val cardSets: List<CardSet?>?,
        @SerializedName("card_images") override val cardImages: List<CardImage?>?,
        @SerializedName("card_prices") val cardPrices: List<CardPrice?>?

    ) : DomainCard(id, networkId, name, cardImages)

    data class DomainNonMonsterCard(
        override val id: Long,
        override val networkId: Long,
        override val name: String,
        val type: String,
        val desc: String,
        val race: String,
        val archetype: String? = null,
        @SerializedName("card_sets") val cardSets: List<CardSet?>?,
        @SerializedName("card_images") override val cardImages: List<CardImage?>?,
        @SerializedName("card_prices") val cardPrices: List<CardPrice?>?

    ) : DomainCard(id, networkId, name, cardImages)
}