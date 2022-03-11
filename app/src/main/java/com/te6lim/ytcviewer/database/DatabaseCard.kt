package com.te6lim.ytcviewer.database

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.te6lim.ytcviewer.network.CardImage
import com.te6lim.ytcviewer.network.CardPrice
import com.te6lim.ytcviewer.network.CardSet

@Entity(tableName = "databaseCard")
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

@Entity(tableName = "databaseCard")
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