package com.fimoney.practical.model.response

import com.fimoney.practical.model.DataModel
import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class DataResponse(
    @Json(name = "Search")
    val dataList: List<DataModel> = emptyList(),

    @Json(name = "totalResults")
    val totalResult: String
) : StatusResponse()
