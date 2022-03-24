package com.te6lim.ytcviewer.database

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "remote_keys")
data class RemoteKey(
    @PrimaryKey val id: Long,
    val nextKey: Int?,
    val prevKey: Int?
)

interface RemoteKeyDao {

    suspend fun
}