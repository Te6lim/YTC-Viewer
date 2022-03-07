package com.te6lim.ytcviewer.models

sealed class Response(
    open val data: List<Card>
) {
    class MonsterCardResponse(
        override val data: List<Card.MonsterCard>
    ) : Response(data)

    class NonMonsterCardResponse(
        override val data: List<Card.NonMonsterCard>
    ) : Response(data)
}