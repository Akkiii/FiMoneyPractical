package com.fimoney.practical.model

import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class DataModel(
    @Json(name = "Title")
    val title: String,

    @Json(name = "Year")
    val year: String,

    @Json(name = "imdbID")
    val imdbID: String,

    @Json(name = "Type")
    val type: String,

    @Json(name = "Poster")
    val poster: String,

    var isBookMark: Boolean = false
)
