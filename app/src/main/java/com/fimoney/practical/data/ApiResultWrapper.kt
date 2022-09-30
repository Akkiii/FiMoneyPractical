package com.fimoney.practical.data

import com.squareup.moshi.JsonDataException
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import timber.log.Timber
import java.io.IOException

suspend fun <T> wrapIntoApiResult(
    dispatcher: CoroutineDispatcher = Dispatchers.Unconfined,
    apiCall: suspend () -> T
): ApiResult<T> {
    return withContext(dispatcher) {
        try {
            ApiResult.Success(apiCall.invoke())
        } catch (throwable: Throwable) {
            throwable.printStackTrace()
            when (throwable) {
                is IOException -> ApiResult.NetworkError
                else -> {
                    if (throwable is retrofit2.HttpException) {
                        val t = HttpException(throwable)
                        return@withContext ApiResult.GenericError(t)
                    }
                    if (throwable is JsonDataException) {
                        Timber.e(throwable, "JSON parsing failed.")
                    }
                    ApiResult.GenericError(throwable)
                }
            }
        }
    }
}

class HttpException(throwable: retrofit2.HttpException) :
    RuntimeException(buildMessage(throwable)) {

    companion object {
        private fun buildMessage(throwable: retrofit2.HttpException): String {
            val body = convertErrorBodyToString(throwable)
            return if (body != null) {
                "HTTP ${throwable.code()} ${throwable.message()} $body"
            } else {
                "HTTP ${throwable.code()} ${throwable.message()}"
            }
        }

        private fun convertErrorBodyToString(throwable: retrofit2.HttpException): String? {
            return try {
                throwable.response()
                    ?.errorBody()
                    ?.string()
            } catch (_: Throwable) {
                null
            }
        }
    }
}
