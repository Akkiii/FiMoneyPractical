package com.fimoney.practical.data.respository

import com.fimoney.practical.data.ApiResult
import com.fimoney.practical.model.DataModel
import com.fimoney.practical.model.response.DataResponse
import com.fimoney.practical.model.response.MovieDetailResponse
import kotlinx.coroutines.flow.Flow

interface FiMoneyRepository {
    suspend fun getData(map: Map<String, String>): ApiResult<DataResponse>

    suspend fun getMovieDetail(map: Map<String, String>): ApiResult<MovieDetailResponse>

    suspend fun addToBookMark(result: DataModel)

    suspend fun getBookMark(): Flow<List<DataModel>>

    suspend fun removeBookMark(result: DataModel)
}