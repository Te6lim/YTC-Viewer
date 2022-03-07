package com.te6lim.ytcviewer.models

open class Response(
    open val data: List<Card>
) {
    class MonsterCardResponse(
        override val data: List<Card.MonsterCard>
    ) : Response(data)

    class NonMonsterCardResponse(
        override val data: List<Card.NonMonsterCard>
    ) : Response(data)
}