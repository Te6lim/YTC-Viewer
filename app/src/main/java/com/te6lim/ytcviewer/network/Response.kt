package com.te6lim.ytcviewer.network

sealed class Response(
    open val data: List<NetworkCard>
) {
    class MonsterCardResponse(
        override val data: List<NetworkCard.MonsterNetworkCard>
    ) : Response(data)

    class NonMonsterCardResponse(
        override val data: List<NetworkCard.NonMonsterNetworkCard>
    ) : Response(data)
}