package com.fimoney.practical.db

import androidx.room.*
import kotlinx.coroutines.flow.Flow

@Dao
interface BookmarkDao {

    @Query("SELECT * FROM bookmark_movies")
    fun getBookMarkResults(): Flow<List<BookmarkEntity>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(bookmarkEntity: BookmarkEntity)

    @Delete
    fun deleteBookMark(bookmarkEntity: BookmarkEntity)
}
