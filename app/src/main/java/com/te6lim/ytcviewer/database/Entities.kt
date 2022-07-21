package com.te6lim.ytcviewer.database

import android.os.Parcelable
import androidx.lifecycle.LiveData
import androidx.paging.PagingSource
import androidx.room.*
import com.te6lim.ytcviewer.model.UiItem
import com.te6lim.ytcviewer.network.CardImage
import com.te6lim.ytcviewer.network.CardPrice
import com.te6lim.ytcviewer.network.CardSet
import kotlinx.parcelize.Parcelize

@Entity(tableName = "card")
@Parcelize
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
    @ColumnInfo var favourite: Boolean = false
) : Parcelable {
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
    fun getCard(id: Long): LiveData<Card?>

    @Query("SELECT * FROM card")
    fun getSource(): PagingSource<Int, Card>

    @Query("SELECT * FROM card WHERE favourite = :isFavorite ORDER BY id DESC")
    fun getFavorites(isFavorite: Boolean = true): LiveData<List<Card>?>

    @Query("DELETE FROM card")
    suspend fun clear()

    @Query("DELETE FROM card WHERE networkId = :id")
    suspend fun deleteCard(id: Long)
}

@Entity(tableName = "remote_keys")
data class RemoteKey(
    @PrimaryKey val id: Long,
    val key: Int?,
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

fun Card.toUiItem(): UiItem {
    return UiItem.CardItem(this)
}