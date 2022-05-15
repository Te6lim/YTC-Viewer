package com.te6lim.ytcviewer.database

import androidx.paging.PagingSource
import androidx.room.*
import com.te6lim.ytcviewer.domain.DomainCard
import com.te6lim.ytcviewer.network.CardImage
import com.te6lim.ytcviewer.network.CardPrice
import com.te6lim.ytcviewer.network.CardSet

@Entity(tableName = "card")
data class DatabaseCard(
    @PrimaryKey(autoGenerate = true) val id: Long = 0L,
    @ColumnInfo val networkId: Long,
    @ColumnInfo val name: String,
    @ColumnInfo val type: String?,
    @ColumnInfo val desc: String = "",
    @ColumnInfo val race: String?,
    @ColumnInfo val atk: Int?,
    @ColumnInfo val def: Int?,
    @ColumnInfo val level: Int?,
    @ColumnInfo val attribute: String?,
    @ColumnInfo val archetype: String?,
    @ColumnInfo val cardSets: List<CardSet?>?,
    @ColumnInfo val cardImages: List<CardImage?>?,
    @ColumnInfo val cardPrices: List<CardPrice?>?
)

@Dao
interface CardDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(card: DatabaseCard): Long

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertMany(cards: List<DatabaseCard>): List<Long>

    @Query("SELECT * FROM card")
    fun getSource(): PagingSource<Int, DatabaseCard>

    @Query("DELETE FROM card")
    suspend fun clear()
}

@Entity(tableName = "remote_keys")
data class RemoteKey(
    @PrimaryKey val id: Long,
    val nextKey: Int?,
    val prevKey: Int?
)

@Dao
interface RemoteKeysDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertMany(keys: List<RemoteKey>)

    @Query("SELECT * FROM remote_keys WHERE id = :remoteKeyId LIMIT 1")
    suspend fun get(remoteKeyId: Long): RemoteKey?

    @Query("DELETE FROM remote_keys")
    suspend fun clear()
}

fun DatabaseCard.toDomainCard(): DomainCard {
    return DomainCard(
        id = id, networkId = networkId, name = name, type = type, desc = desc, race = race, atk = atk,
        def = def, level = level, attribute = attribute, archetype = archetype, cardSets = cardSets,
        cardImages = cardImages, cardPrices = cardPrices
    )
}