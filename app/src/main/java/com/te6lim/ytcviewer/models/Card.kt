package com.te6lim.ytcviewer.models

import com.squareup.moshi.Json

open class Card(
    open val name: String,
    open val type: String,
    open val desc: String,
    open val race: String,
    @Json(name = "card_sets") open val cardSets: List<CardSet?>?,
    @Json(name = "card_images") open val cardImages: List<CardImages?>?,
    @Json(name = "card_prices") open val cardPrices: List<CardPrice?>?
) {

    data class MonsterCard(
        override val name: String,
        override val type: String,
        override val desc: String,
        override val race: String,
        val atk: Int,
        val def: Int,
        val level: Int,
        val attribute: String,
        @Json(name = "card_sets") override val cardSets: List<CardSet?>?,
        @Json(name = "card_images") override val cardImages: List<CardImages?>?,
        @Json(name = "card_prices") override val cardPrices: List<CardPrice?>?

    ) : Card(name, type, desc, race, cardSets, cardImages, cardPrices)

    data class NonMonsterCard(
        override val name: String,
        override val type: String,
        override val desc: String,
        override val race: String,
        val archetype: String,
        @Json(name = "card_sets") override val cardSets: List<CardSet?>?,
        @Json(name = "card_images") override val cardImages: List<CardImages?>?,
        @Json(name = "card_prices") override val cardPrices: List<CardPrice?>?
    ) : Card(name, type, desc, race, cardSets, cardImages, cardPrices)
}

class CardSet(
    val setName: String? = null,
    val setCode: String? = null,
    val setRarity: String? = null,
    val setRarityCode: String? = null,
    val setPrice: String? = null
)

class CardImages(
    @Json(name = "image_url") val imageUrl: String? = null,
    @Json(name = "image_url_small") val imageUrlSmall: String? = null
)

class CardPrice(
    val cardMarketPrice: String? = null,
    val tcgPlayerPrice: String? = null,
    val ebayPrice: String? = null,
    val amazonPrice: String? = null,
    val coolStuffIncPrice: String? = null
)