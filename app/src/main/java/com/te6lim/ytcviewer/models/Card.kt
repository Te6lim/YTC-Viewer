package com.te6lim.ytcviewer.models

sealed class Card(
    open val name: String,
    open val type: String,
    open val desc: String,
    open val race: String,
    open val cardSets: List<CardSet>,
    open val cardImages: CardImages,
    open val cardPrices: CardPrice,
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
        override val cardSets: List<CardSet>,
        override val cardImages: CardImages,
        override val cardPrices: CardPrice

    ) : Card(name, type, desc, race, cardSets, cardImages, cardPrices)

    data class NonMonsterCard(
        override val name: String,
        override val type: String,
        override val desc: String,
        override val race: String,
        val archetype: String,
        override val cardSets: List<CardSet>,
        override val cardImages: CardImages,
        override val cardPrices: CardPrice
    ) : Card(name, type, desc, race, cardSets, cardImages, cardPrices)
}

class CardSet(
    val setName: String,
    val setCode: String,
    val setRarity: String,
    val setRarityCode: String,
    val setPrice: String
)

class CardImages(
    val imageUrl: String,
    val imageUrlSmall: String
)

class CardPrice(
    val cardMarketPrice: String,
    val tcgPlayerPrice: String,
    val ebayPrice: String,
    val amazonPrice: String,
    val coolStuffIncPrice: String
)