package com.te6lim.ytcviewer.database

import androidx.lifecycle.LiveData
import androidx.room.*
import com.te6lim.ytcviewer.model.UiItem
import com.te6lim.ytcviewer.network.CardImage
import com.te6lim.ytcviewer.network.CardPrice
import com.te6lim.ytcviewer.network.CardSet

@Entity(tableName = "card")
data class Card(
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
    @ColumnInfo val cardPrices: List<CardPrice?>?,
    @ColumnInfo var isFavourite: Boolean = false
) {
    fun isNonMonsterCard(): Boolean {
        return atk == null && def == null && level == null && attribute == null
    }
}

@Dao
interface CardDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(card: Card): Long

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertMany(cards: List<Card>): List<Long>

    @Query("SELECT * FROM card WHERE networkId = :id")
    suspend fun getCard(id: Long): Card?

    @Query("SELECT * FROM card ORDER BY id DESC")
    fun getAll(): LiveData<List<Card>?>

    @Query("DELETE FROM card")
    suspend fun clear()

    @Query("DELETE FROM card WHERE networkId = :id")
    suspend fun deleteCard(id: Long)
}

fun Card.toUiItem(): UiItem {
    return UiItem.CardItem(this)
}