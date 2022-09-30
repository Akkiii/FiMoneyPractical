package com.fimoney.practical.data.respository

import com.fimoney.practical.data.ApiResult
import com.fimoney.practical.data.remote.FiMoneyApiService
import com.fimoney.practical.data.wrapIntoApiResult
import com.fimoney.practical.db.BookmarkDao
import com.fimoney.practical.db.BookmarkEntity
import com.fimoney.practical.model.DataModel
import com.fimoney.practical.model.response.DataResponse
import com.fimoney.practical.model.response.MovieDetailResponse
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

class DefaultFiMoneyRepository(
    private val apiService: FiMoneyApiService,
    private val bookmarkDao: BookmarkDao,
    private val dispatcher: CoroutineDispatcher = Dispatchers.IO
) : FiMoneyRepository {

    override suspend fun getData(map: Map<String, String>): ApiResult<DataResponse> {
        return wrapIntoApiResult(dispatcher) {
            apiService.getData(map)
        }
    }

    override suspend fun getMovieDetail(map: Map<String, String>): ApiResult<MovieDetailResponse> {
        return wrapIntoApiResult(dispatcher) {
            apiService.getMovieDetail(map)
        }
    }

    override suspend fun addToBookMark(result: DataModel) {
        bookmarkDao.insert(
            bookmarkEntity = BookmarkEntity.from(item = result)
        )
    }

    override suspend fun getBookMark(): Flow<List<DataModel>> {
        return bookmarkDao.getBookMarkResults().map { results ->
            results.map { entity ->
                entity.toBookMarkResult()
            }
        }
    }

    override suspend fun removeBookMark(result: DataModel) {
        bookmarkDao.deleteBookMark(bookmarkEntity = BookmarkEntity.from(result))
    }
}