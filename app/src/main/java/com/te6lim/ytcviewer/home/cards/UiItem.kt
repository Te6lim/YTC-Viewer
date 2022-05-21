package com.te6lim.ytcviewer.home.cards

import com.te6lim.ytcviewer.database.Card

sealed class UiItem {

    abstract val id: Long

    class CardItem(val card: Card) : UiItem() {
        override val id: Long = card.id
    }

    object Footer : UiItem() {
        override val id: Long = Long.MAX_VALUE
    }
}