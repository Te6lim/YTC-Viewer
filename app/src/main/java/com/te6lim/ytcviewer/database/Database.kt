package com.te6lim.ytcviewer.database

import android.content.Context
import androidx.room.*
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
    @ColumnInfo val cardSets: List<CardSet?>? = null,
    @ColumnInfo val cardImages: List<CardImage?>? = null,
    @ColumnInfo val cardPrice: List<CardPrice?>? = null
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
    @ColumnInfo val cardPrice: List<CardPrice?>? = null
)

@Dao
interface MonsterDao {

    @Insert
    fun insert(card: DatabaseMonsterCard): Long

    @Insert
    fun insertMany(vararg card: DatabaseMonsterCard)

    @Update
    fun update(card: DatabaseMonsterCard)

    @Query("SELECT * FROM monsterDatabaseCard WHERE id = :key")
    fun get(key: Long)

    @Query("DELETE FROM monsterDatabaseCard")
    fun clear()
}

@Dao
interface NonMonsterDao {

    @Insert
    fun insert(card: DatabaseNonMonsterCard): Long

    @Insert
    fun insertMany(vararg card: DatabaseNonMonsterCard)

    @Update
    fun update(card: DatabaseNonMonsterCard)

    @Query("SELECT * FROM nonMonsterDatabaseCard WHERE id = :key")
    fun get(key: Long)

    @Query("DELETE FROM nonMonsterDatabaseCard")
    fun clear()
}

@Database(
    entities = [DatabaseMonsterCard::class, DatabaseNonMonsterCard::class],
    version = 1, exportSchema = false
)
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
                    ).build()
                    INSTANCE = instance
                }
                return instance
            }
        }
    }
}