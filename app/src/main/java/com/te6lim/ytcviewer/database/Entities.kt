package com.te6lim.ytcviewer.database

import androidx.paging.PagingSource
import androidx.room.*
import com.te6lim.ytcviewer.domain.DomainCard
import com.te6lim.ytcviewer.network.CardImage
import com.te6lim.ytcviewer.network.CardPrice
import com.te6lim.ytcviewer.network.CardSet
import com.te6lim.ytcviewer.repository.CardType
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

@Entity(tableName = "card")
data class DatabaseCard(
    @PrimaryKey(autoGenerate = true) val id: Long = 0L,
    @ColumnInfo val networkId: Long,
    @ColumnInfo val name: String,
    @ColumnInfo val cardImages: List<CardImage?>? = null
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

@Entity(tableName = "monsterDatabaseCard")
data class DatabaseMonsterCard(
    @PrimaryKey val id: Long,
    @ColumnInfo val networkId: Long,
    @ColumnInfo val name: String,
    @ColumnInfo val type: String,
    @ColumnInfo val desc: String,
    @ColumnInfo val race: String,
    @ColumnInfo val atk: Int? = null,
    @ColumnInfo val def: Int? = null,
    @ColumnInfo val level: Int? = null,
    @ColumnInfo val attribute: String,
    @ColumnInfo val cardSets: List<CardSet?>? = null,
    @ColumnInfo val cardImages: List<CardImage?>? = null,
    @ColumnInfo val cardPrices: List<CardPrice?>? = null
)

@Entity(tableName = "nonMonsterDatabaseCard")
data class DatabaseNonMonsterCard(
    @PrimaryKey val id: Long,
    @ColumnInfo val networkId: Long,
    @ColumnInfo val name: String,
    @ColumnInfo val type: String,
    @ColumnInfo val desc: String,
    @ColumnInfo val race: String,
    @ColumnInfo val archetype: String? = null,
    @ColumnInfo val cardSets: List<CardSet?>? = null,
    @ColumnInfo val cardImages: List<CardImage?>? = null,
    @ColumnInfo val cardPrices: List<CardPrice?>? = null
)

@Dao
interface MonsterDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(card: DatabaseMonsterCard): Long

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertMany(cards: List<DatabaseMonsterCard>): List<Long>

    @Update
    suspend fun update(card: DatabaseMonsterCard)

    @Query("SELECT * FROM monsterDatabaseCard WHERE id = :key")
    suspend fun get(key: Long): DatabaseMonsterCard?

    @Query("SELECT * FROM monsterDatabaseCard ORDER BY name ASC")
    suspend fun getAll(): List<DatabaseMonsterCard>?

    @Query("SELECT * FROM monsterDatabaseCard")
    fun getSource(): PagingSource<Int, DatabaseMonsterCard>

    @Query("DELETE FROM monsterDatabaseCard")
    suspend fun clear()
}

@Dao
interface NonMonsterDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(card: DatabaseNonMonsterCard): Long

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertMany(cards: List<DatabaseNonMonsterCard>): List<Long>

    @Update
    suspend fun update(card: DatabaseNonMonsterCard)

    @Query("SELECT * FROM nonMonsterDatabaseCard WHERE id = :key")
    suspend fun get(key: Long): DatabaseNonMonsterCard?

    @Query("SELECT * FROM nonMonsterDatabaseCard ORDER BY name ASC")
    suspend fun getAll(): List<DatabaseNonMonsterCard>?

    @Query("DELETE FROM nonMonsterDatabaseCard")
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

/*fun List<DatabaseMonsterCard>.toDomainMonsterCards(): List<DomainCard.DomainMonsterCard> {
    return map {
        DomainCard.DomainMonsterCard(
            id = it.id, name = it.name, type = it.type, desc = it.desc, race = it.race,
            atk = it.atk, def = it.def, level = it.level, attribute = it.attribute,
            cardSets = it.cardSets, cardImages = it.cardImages, cardPrices = it.cardPrices
        )
    }
}

fun List<DatabaseNonMonsterCard>.toDomainNonMonsterCards(): List<DomainCard.DomainNonMonsterCard> {
    return map {
        DomainCard.DomainNonMonsterCard(
            id = it.id, name = it.name, desc = it.desc, type = it.type, race = it.race,
            archetype = it.archetype, cardSets = it.cardSets, cardImages = it.cardImages,
            cardPrices = it.cardPrices
        )
    }
}

fun List<DatabaseMonsterCard>.toDatabaseCard(): List<DatabaseCard> {
    return map {
        it as DatabaseCard
    }
}*/

suspend fun DatabaseCard.toDomainCard(db: CardDatabase, cardType: CardType): DomainCard {
    return withContext(Dispatchers.IO) {
        DomainCard(id = id, networkId = networkId, name, cardImages)
    }
}