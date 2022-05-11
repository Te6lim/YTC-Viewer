package com.te6lim.ytcviewer.network

import com.squareup.moshi.Json

open class Response(
    open val data: List<NetworkCard>,
    open val meta: CardMetaData
) {
    class MonsterCardResponse(
        override val data: List<NetworkCard.NetworkMonsterCard>,
        override val meta: CardMetaData
    ) : Response(data, meta)

    class NonMonsterCardResponse(
        override val data: List<NetworkCard.NetworkNonMonsterCard>,
        override val meta: CardMetaData
    ) : Response(data, meta)
}

class CardMetaData(
    @Json(name = "current_rows") val currentRows: Int,
    @Json(name = "total_rows") val totalRows: Int,
    @Json(name = "rows_remaining") val rowsRemaining: Int,
    @Json(name = "total_pages") val totalPages: Int,
    @Json(name = "paes_remaining") val pagesRemaining: Int,
    @Json(name = "next_page") val nextPage: String,
    @Json(name = "next_page_offset") val nextPageOffset: Int
)
