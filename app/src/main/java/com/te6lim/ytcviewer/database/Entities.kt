package com.te6lim.ytcviewer.database

import androidx.paging.PagingSource
import androidx.room.*
import com.te6lim.ytcviewer.domain.DomainCard
import com.te6lim.ytcviewer.network.CardImage
import com.te6lim.ytcviewer.network.CardPrice
import com.te6lim.ytcviewer.network.CardSet

@Entity()
open class DatabaseCard(
    open val id: Long,
    open val position: Long,
    open val name: String,
    open val type: String,
    open val desc: String,
    open val race: String,
    open val cardSets: List<CardSet?>? = null,
    open val cardImages: List<CardImage?>? = null,
    open val cardPrices: List<CardPrice?>? = null
)

@Entity(tableName = "monsterDatabaseCard")
data class DatabaseMonsterCard(
    @PrimaryKey(autoGenerate = false) override val id: Long,
    @ColumnInfo override val position: Long,
    @ColumnInfo override val name: String,
    @ColumnInfo override val type: String,
    @ColumnInfo override val desc: String,
    @ColumnInfo override val race: String,
    @ColumnInfo val atk: Int? = null,
    @ColumnInfo val def: Int? = null,
    @ColumnInfo val level: Int? = null,
    @ColumnInfo val attribute: String,
    @ColumnInfo override val cardSets: List<CardSet?>? = null,
    @ColumnInfo override val cardImages: List<CardImage?>? = null,
    @ColumnInfo override val cardPrices: List<CardPrice?>? = null
) : DatabaseCard(id, position, name, type, desc, race, cardSets, cardImages, cardPrices)

@Entity(tableName = "nonMonsterDatabaseCard")
data class DatabaseNonMonsterCard(
    @PrimaryKey(autoGenerate = false) override val id: Long,
    @ColumnInfo override val position: Long,
    @ColumnInfo override val name: String,
    @ColumnInfo override val type: String,
    @ColumnInfo override val desc: String,
    @ColumnInfo override val race: String,
    @ColumnInfo val archetype: String? = null,
    @ColumnInfo override val cardSets: List<CardSet?>? = null,
    @ColumnInfo override val cardImages: List<CardImage?>? = null,
    @ColumnInfo override val cardPrices: List<CardPrice?>? = null
) : DatabaseCard(id, position, name, type, desc, race, cardSets, cardImages, cardPrices)

@Dao
interface MonsterDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(card: DatabaseMonsterCard): Long

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertMany(vararg card: DatabaseMonsterCard)

    @Update
    suspend fun update(card: DatabaseMonsterCard)

    @Query("SELECT * FROM monsterDatabaseCard WHERE id = :key")
    suspend fun get(key: Long): DatabaseMonsterCard?

    @Query("SELECT * FROM monsterDatabaseCard ORDER BY name ASC")
    suspend fun getAll(): List<DatabaseMonsterCard>?

    @Query("SELECT * FROM monsterDatabaseCard WHERE position BETWEEN :top AND :bottom")
    suspend fun getAllInRange(top: Int, bottom: Int): List<DatabaseMonsterCard>?

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
    suspend fun insertMany(vararg card: DatabaseNonMonsterCard)

    @Update
    suspend fun update(card: DatabaseNonMonsterCard)

    @Query("SELECT * FROM nonMonsterDatabaseCard WHERE id = :key")
    suspend fun get(key: Long): DatabaseNonMonsterCard?

    @Query("SELECT * FROM nonMonsterDatabaseCard ORDER BY name ASC")
    suspend fun getAll(): List<DatabaseNonMonsterCard>?

    @Query("SELECT * FROM nonMonsterDatabaseCard WHERE position BETWEEN :top AND :bottom")
    suspend fun getAllInRange(top: Int, bottom: Int): List<DatabaseNonMonsterCard>?

    @Query("DELETE FROM nonMonsterDatabaseCard")
    suspend fun clear()
}

fun List<DatabaseMonsterCard>.toDomainMonsterCards(): List<DomainCard.DomainMonsterCard> {
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
}

fun DatabaseCard.toDomainCard(): DomainCard {
    return DomainCard(id, name, type, desc, race, cardSets, cardImages, cardPrices)
}