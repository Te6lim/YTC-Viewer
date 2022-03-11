package com.te6lim.ytcviewer.domain

import com.google.gson.annotations.SerializedName
import com.te6lim.ytcviewer.network.CardImage
import com.te6lim.ytcviewer.network.CardPrice
import com.te6lim.ytcviewer.network.CardSet

sealed class DomainCard(
    open val id: Long,
    open val name: String,
    open val type: String,
    open val desc: String,
    open val race: String,
    open val cardSets: List<CardSet?>?,
    open val cardImages: List<CardImage?>?,
    open val cardPrices: List<CardPrice?>?
) {

    data class MonsterCard(
        override val id: Long,
        override val name: String,
        override val type: String,
        override val desc: String,
        override val race: String,
        val atk: Int? = null,
        val def: Int? = null,
        val level: Int? = null,
        val attribute: String,
        @SerializedName("card_sets") override val cardSets: List<CardSet?>?,
        @SerializedName("card_images") override val cardImages: List<CardImage?>?,
        @SerializedName("card_prices") override val cardPrices: List<CardPrice?>?

    ) : DomainCard(id, name, type, desc, race, cardSets, cardImages, cardPrices)

    data class NonMonsterCard(
        override val id: Long,
        override val name: String,
        override val type: String,
        override val desc: String,
        override val race: String,
        val archetype: String? = null,
        @SerializedName("card_sets") override val cardSets: List<CardSet?>?,
        @SerializedName("card_images") override val cardImages: List<CardImage?>?,
        @SerializedName("card_prices") override val cardPrices: List<CardPrice?>?

    ) : DomainCard(id, name, type, desc, race, cardSets, cardImages, cardPrices)
}