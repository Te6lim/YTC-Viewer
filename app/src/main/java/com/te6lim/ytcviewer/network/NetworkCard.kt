package com.te6lim.ytcviewer.network

import android.os.Parcelable
import com.squareup.moshi.Json
import com.te6lim.ytcviewer.database.Card
import kotlinx.parcelize.Parcelize

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
)

@Parcelize
class CardSet(
    @Json(name = "set_name") val setName: String? = null,
    @Json(name = "set_code") val setCode: String? = null,
    @Json(name = "set_rarity") val setRarity: String? = null,
    @Json(name = "set_rarity_code") val setRarityCode: String? = null,
    @Json(name = "set_price") val setPrice: String? = null
) : Parcelable

@Parcelize
class CardImage(
    @Json(name = "image_url") val imageUrl: String? = null,
    @Json(name = "image_url_small") val imageUrlSmall: String? = null
) : Parcelable

@Parcelize
class CardPrice(
    @Json(name = "card_market_price") val cardMarketPrice: String? = null,
    @Json(name = "tcgplayer_price") val tcgPlayerPrice: String? = null,
    @Json(name = "ebay_price") val ebayPrice: String? = null,
    @Json(name = "amazon_price") val amazonPrice: String? = null,
    @Json(name = "collstuffinc_price") val coolStuffIncPrice: String? = null
) : Parcelable

fun List<NetworkCard>.toLocalCard(isAsc: Boolean): List<Card> {
    val list = map {
        Card(
            networkId = it.id, name = it.name, type = it.type, desc = it.desc, race = it.race, atk = it.atk,
            def = it.def, level = it.level, attribute = it.attribute, archetype = it.archetype,
            cardSets = it.cardSets, cardImages = it.cardImages, cardPrices = it.cardPrices
        )
    }
    return if (!isAsc) list.reversed() else list
}