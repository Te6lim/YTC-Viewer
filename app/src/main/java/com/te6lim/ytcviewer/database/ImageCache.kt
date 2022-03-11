package com.te6lim.ytcviewer.database

import android.media.Image
import androidx.room.*

@Entity(tableName = "image_table")
data class ImageCache(
    @PrimaryKey(autoGenerate = true) val id: Long,
    @ColumnInfo val image: Image? = null,
    @ColumnInfo val imageSmall: Image? = null
)

@Dao
interface ImageCacheDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insert(image: ImageCache): Long
}