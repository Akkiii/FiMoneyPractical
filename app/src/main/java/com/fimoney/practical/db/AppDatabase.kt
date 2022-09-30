package com.fimoney.practical.db

import android.app.Application
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase

@Database(entities = [BookmarkEntity::class], version = 1, exportSchema = false)
abstract class AppDatabase : RoomDatabase() {

    companion object {
        fun create(application: Application) =
            Room.databaseBuilder(application, AppDatabase::class.java, "fimoney")
                .fallbackToDestructiveMigration().build()
    }

    abstract fun bookmarkDao(): BookmarkDao
}
