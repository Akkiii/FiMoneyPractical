package com.fimoney.practical.db

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.fimoney.practical.model.DataModel

@Entity(tableName = "bookmark_movies")
data class BookmarkEntity(

    @PrimaryKey
    val uid: Int,

    @ColumnInfo(name = "Title")
    val title: String,

    @ColumnInfo(name = "Year")
    val year: String,

    @ColumnInfo(name = "imdbID")
    val imdbID: String,

    @ColumnInfo(name = "Type")
    val type: String,

    @ColumnInfo(name = "Poster")
    val poster: String
) {
    companion object {
        fun from(item: DataModel) = BookmarkEntity(
            uid = item.hashCode(),
            title = item.title,
            year = item.year,
            imdbID = item.imdbID,
            type = item.type,
            poster = item.poster
        )
    }

    fun toBookMarkResult(): DataModel {
        return DataModel(
            title = title,
            year = year,
            imdbID = imdbID,
            type = type,
            poster = poster
        )
    }
}