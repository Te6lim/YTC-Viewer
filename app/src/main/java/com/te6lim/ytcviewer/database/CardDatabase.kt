package com.te6lim.ytcviewer.database

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.TypeConverters

@Database(
    entities = [DatabaseMonsterCard::class, DatabaseNonMonsterCard::class, RemoteKey::class],
    version = 1, exportSchema = false
)
@TypeConverters(Converter::class)
abstract class CardDatabase : RoomDatabase() {

    abstract val monsterDao: MonsterDao
    abstract val nonMonsterDao: NonMonsterDao
    abstract val remoteKeysDao: RemoteKeysDao

    companion object {

        @Volatile
        private var INSTANCE: CardDatabase? = null

        fun getInstance(context: Context): com.te6lim.ytcviewer.database.CardDatabase {
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