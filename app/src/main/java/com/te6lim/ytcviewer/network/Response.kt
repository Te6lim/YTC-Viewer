package com.te6lim.ytcviewer.network

open class Response(
    open val data: List<NetworkCard>
) {
    class MonsterCardResponse(
        override val data: List<NetworkCard.NetworkMonsterCard>
    ) : Response(data)

    class NonMonsterCardResponse(
        override val data: List<NetworkCard.NetworkNonMonsterCard>
    ) : Response(data)
}