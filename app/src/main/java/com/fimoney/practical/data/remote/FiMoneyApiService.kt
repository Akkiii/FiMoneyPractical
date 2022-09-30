package com.fimoney.practical.data.remote

import com.fimoney.practical.model.response.DataResponse
import com.fimoney.practical.model.response.MovieDetailResponse
import retrofit2.http.GET
import retrofit2.http.QueryMap

interface FiMoneyApiService {
    @GET("/")
    suspend fun getData(@QueryMap queryData: Map<String, String>): DataResponse

    @GET("/")
    suspend fun getMovieDetail(@QueryMap queryData: Map<String, String>): MovieDetailResponse
}