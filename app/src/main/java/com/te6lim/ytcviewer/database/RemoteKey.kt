package com.te6lim.ytcviewer.database

import androidx.room.Entity
import androidx.room.PrimaryKey
import androidx.room.Query

@Entity(tableName = "remote_keys")
data class RemoteKey(
    @PrimaryKey val id: Long,
    val nextKey: Int?,
    val prevKey: Int?
)

interface RemoteKeysDao {

    suspend fun insertMany(vararg remoteKeys: RemoteKey)

    @Query("SELECT * FROM remote_keys WHERE id = :remoteKeyId LIMIT 1")
    suspend fun get(remoteKeyId: Long): RemoteKey?

    @Query("DELETE FROM remote_keys")
    suspend fun deleteAll()
}