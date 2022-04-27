package com.example.myapplication.database

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.example.myapplication.api.Keys
import com.example.myapplication.api.ListStory
import com.example.myapplication.data.StoryDao

@Database(
    entities = [ListStory::class, Keys::class],
    version = 1,
    exportSchema = false
)


abstract class StoryDatabase : RoomDatabase() {

    abstract fun StoryDao(): StoryDao
    abstract fun KeysDao(): KeysDao


    companion object {

        @Volatile
        private var INSTANCE: StoryDatabase? = null


        @JvmStatic
        fun getDatabase(context: Context): StoryDatabase {
            return INSTANCE ?: synchronized(this) {
                INSTANCE ?: Room.databaseBuilder(
                    context.applicationContext,
                    StoryDatabase::class.java, "listStory.db"
                )
                    .fallbackToDestructiveMigration()
                    .build()
                    .also { INSTANCE = it }
            }
        }
    }
}