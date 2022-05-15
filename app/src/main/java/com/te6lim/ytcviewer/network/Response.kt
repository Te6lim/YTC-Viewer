package com.te6lim.ytcviewer.network

import com.squareup.moshi.Json

open class Response(
    open val data: List<NetworkCard>,
    open val meta: CardMetaData
)

class CardMetaData(
    @Json(name = "current_rows") val currentRows: Int,
    @Json(name = "total_rows") val totalRows: Int,
    @Json(name = "rows_remaining") val rowsRemaining: Int,
    @Json(name = "total_pages") val totalPages: Int,
    @Json(name = "pages_remaining") val pagesRemaining: Int,
    @Json(name = "next_page") val nextPage: String?,
    @Json(name = "next_page_offset") val nextPageOffset: Int?
)
