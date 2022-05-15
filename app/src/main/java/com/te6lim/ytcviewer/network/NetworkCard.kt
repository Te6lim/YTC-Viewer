package com.te6lim.ytcviewer.network

import com.squareup.moshi.Json
import com.te6lim.ytcviewer.database.DatabaseCard

open class NetworkCard(
    val id: Long,
    val name: String,
    val type: String?,
    val desc: String = "",
    val race: String?,
    val atk: Int? = null,
    val def: Int? = null,
    val level: Int? = null,
    val attribute: String?,
    val archetype: String? = null,
    @Json(name = "card_sets") val cardSets: List<CardSet?>?,
    @Json(name = "card_images") val cardImages: List<CardImage?>?,
    @Json(name = "card_prices") val cardPrices: List<CardPrice?>?
) {

    /*data class NetworkMonsterCard(
        override val id: Long,
        override val name: String,
        val type: String?,
        val desc: String?,
        val race: String?,
        val atk: Int? = null,
        val def: Int? = null,
        val level: Int? = null,
        val attribute: String?,
        @Json(name = "card_sets") val cardSets: List<CardSet?>?,
        @Json(name = "card_images") override val cardImages: List<CardImage?>?,
        @Json(name = "card_prices") val cardPrices: List<CardPrice?>?

    ) : NetworkCard(id, name, cardImages)

    data class NetworkNonMonsterCard(
        override val id: Long,
        override val name: String,
        val type: String?,
        val desc: String?,
        val race: String?,
        val archetype: String? = null,
        @Json(name = "card_sets") val cardSets: List<CardSet?>?,
        @Json(name = "card_images") override val cardImages: List<CardImage?>?,
        @Json(name = "card_prices") val cardPrices: List<CardPrice?>?

    ) : NetworkCard(id, name, cardImages)*/
}

class CardSet(
    val setName: String? = null,
    val setCode: String? = null,
    val setRarity: String? = null,
    val setRarityCode: String? = null,
    val setPrice: String? = null
)

class CardImage(
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

fun List<NetworkCard>.toDatabaseCard(isAsc: Boolean): List<DatabaseCard> {
    val list = map {
        DatabaseCard(
            networkId = it.id, name = it.name, type = it.type, desc = it.desc, race = it.race, atk = it.atk,
            def = it.def, level = it.level, attribute = it.attribute, archetype = it.archetype,
            cardSets = it.cardSets, cardImages = it.cardImages, cardPrices = it.cardPrices
        )
    }
    return if (!isAsc) list.reversed() else list
}