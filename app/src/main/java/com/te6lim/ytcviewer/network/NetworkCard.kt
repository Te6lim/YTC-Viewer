package com.te6lim.ytcviewer.network

import com.squareup.moshi.Json
import com.te6lim.ytcviewer.database.DatabaseMonsterCard
import com.te6lim.ytcviewer.database.DatabaseNonMonsterCard
import com.te6lim.ytcviewer.domain.DomainCard

open class NetworkCard(
    open val id: Long,
    open val name: String,
    open val type: String,
    open val desc: String,
    open val race: String,
    @Json(name = "card_sets") open val cardSets: List<CardSet?>?,
    @Json(name = "card_images") open val cardImages: List<CardImage?>?,
    @Json(name = "card_prices") open val cardPrices: List<CardPrice?>?
) {

    data class NetworkMonsterCard(
        override val id: Long,
        override val name: String,
        override val type: String,
        override val desc: String,
        override val race: String,
        val atk: Int? = null,
        val def: Int? = null,
        val level: Int? = null,
        val attribute: String,
        @Json(name = "card_sets") override val cardSets: List<CardSet?>?,
        @Json(name = "card_images") override val cardImages: List<CardImage?>?,
        @Json(name = "card_prices") override val cardPrices: List<CardPrice?>?

    ) : NetworkCard(id, name, type, desc, race, cardSets, cardImages, cardPrices)

    data class NetworkNonMonsterCard(
        override val id: Long,
        override val name: String,
        override val type: String,
        override val desc: String,
        override val race: String,
        val archetype: String? = null,
        @Json(name = "card_sets") override val cardSets: List<CardSet?>?,
        @Json(name = "card_images") override val cardImages: List<CardImage?>?,
        @Json(name = "card_prices") override val cardPrices: List<CardPrice?>?

    ) : NetworkCard(id, name, type, desc, race, cardSets, cardImages, cardPrices)
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

fun List<NetworkCard>.toDatabaseMonsterCards(): List<DatabaseMonsterCard> {
    var p = 0L
    return map {
        it as NetworkCard.NetworkMonsterCard
        DatabaseMonsterCard(
            name = it.name, type = it.type, desc = it.desc, race = it.race,
            atk = it.atk, def = it.def, level = it.level, attribute = it.attribute,
            cardSets = it.cardSets, cardImages = it.cardImages, cardPrices = it.cardPrices
        )
    }
}

fun NetworkCard.toDatabaseMonsterCard(): DatabaseMonsterCard {
    this as NetworkCard.NetworkMonsterCard
    return DatabaseMonsterCard(
        name = this.name, type = this.type, desc = this.desc, race = this.race,
        atk = this.atk, def = this.def, level = this.level, attribute = this.attribute,
        cardSets = this.cardSets, cardImages = this.cardImages, cardPrices = this.cardPrices
    )
}

fun List<NetworkCard>.toDatabaseNonMonsterCards(): List<DatabaseNonMonsterCard> {
    var p = 0L
    return map {
        it as NetworkCard.NetworkNonMonsterCard
        DatabaseNonMonsterCard(
            name = it.name, desc = it.desc, type = it.type, race = it.race,
            archetype = it.archetype, cardSets = it.cardSets, cardImages = it.cardImages,
            cardPrices = it.cardPrices
        )
    }
}

fun List<NetworkCard>.toDomainCards(): List<DomainCard> {
    return map {
        DomainCard(
            id = it.id, name = it.name, type = it.type, desc = it.desc, race = it.race,
            cardSets = it.cardSets, cardImages = it.cardImages, cardPrices = it.cardPrices
        )
    }
}