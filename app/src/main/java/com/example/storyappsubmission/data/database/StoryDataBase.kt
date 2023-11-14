package com.example.storyappsubmission.data.database

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.example.storyappsubmission.data.enitity.RemoteEntity
import com.example.storyappsubmission.data.enitity.StoryEntity


@Database(
    entities = [StoryEntity::class, RemoteEntity::class],
    version = 1,
    exportSchema = false
)
abstract class StoryDataBase: RoomDatabase() {
    abstract fun storyDao(): Dao
    abstract fun remoteKeysDao(): RemoteDao


    companion object {
        @Volatile
        private var INSTANCE: StoryDataBase? = null

        @JvmStatic
        fun getDatabase(context: Context): StoryDataBase {
            return INSTANCE ?: synchronized(this) {
                INSTANCE ?: Room.databaseBuilder(
                    context.applicationContext,
                    StoryDataBase::class.java, "quote_database"
                )
                    .fallbackToDestructiveMigration()
                    .build()
                    .also { INSTANCE = it }
            }
        }
    }

}