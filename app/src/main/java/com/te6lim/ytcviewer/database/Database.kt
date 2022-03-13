package com.te6lim.ytcviewer.database

import android.content.Context
import androidx.lifecycle.LiveData
import androidx.room.*
import com.te6lim.ytcviewer.domain.DomainCard
import com.te6lim.ytcviewer.network.CardImage
import com.te6lim.ytcviewer.network.CardPrice
import com.te6lim.ytcviewer.network.CardSet

@Entity(tableName = "monsterDatabaseCard")
data class DatabaseMonsterCard(
    @PrimaryKey(autoGenerate = false) val id: Long,
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
    @PrimaryKey(autoGenerate = false) val id: Long,
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
    fun insert(card: DatabaseMonsterCard): Long

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertMany(vararg card: DatabaseMonsterCard)

    @Update
    fun update(card: DatabaseMonsterCard)

    @Query("SELECT * FROM monsterDatabaseCard WHERE id = :key")
    fun get(key: Long): DatabaseMonsterCard

    @Query("SELECT * FROM monsterDatabaseCard")
    fun getAll(): LiveData<List<DatabaseMonsterCard>>

    @Query("DELETE FROM monsterDatabaseCard")
    fun clear()
}

@Dao
interface NonMonsterDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insert(card: DatabaseNonMonsterCard): Long

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertMany(vararg card: DatabaseNonMonsterCard)

    @Update
    fun update(card: DatabaseNonMonsterCard)

    @Query("SELECT * FROM nonMonsterDatabaseCard WHERE id = :key")
    fun get(key: Long): DatabaseNonMonsterCard

    @Query("SELECT * FROM nonMonsterDatabaseCard")
    fun getAll(): LiveData<List<DatabaseNonMonsterCard>>

    @Query("DELETE FROM nonMonsterDatabaseCard")
    fun clear()
}

@Database(
    entities = [DatabaseMonsterCard::class, DatabaseNonMonsterCard::class],
    version = 1, exportSchema = false
)
@TypeConverters(Converter::class)
abstract class CardDatabase : RoomDatabase() {

    abstract val monsterDao: MonsterDao
    abstract val nonMonsterDao: NonMonsterDao

    companion object {

        @Volatile
        private var INSTANCE: CardDatabase? = null

        fun getInstance(context: Context): CardDatabase {
            synchronized(this) {
                var instance = INSTANCE
                if (instance == null) {
                    instance = Room.databaseBuilder(
                        context, CardDatabase::class.java, "cardDatabase"
                    ).addTypeConverter(Converter()).build()
                    INSTANCE = instance
                }
                return instance
            }
        }
    }
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