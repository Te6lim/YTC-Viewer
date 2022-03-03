package com.te6lim.ytcviewer.database

import android.media.Image
import androidx.room.*

@Entity(tableName = "image_table")
data class ImageCache(
    @PrimaryKey(autoGenerate = false) val id: Long,
    @ColumnInfo(name = "image") val image: Image? = null
)

@Dao
interface ImageCacheDao {

    @Insert
    fun insert(image: ImageCache): Long
}