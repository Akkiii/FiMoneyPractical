package com.fimoney.practical.data

sealed class ApiResult<out T> {
    data class Success<out T>(val value: T) : ApiResult<T>()
    data class GenericError(val throwable: Throwable? = null) : ApiResult<Nothing>()
    object NetworkError : ApiResult<Nothing>()

    override fun toString(): String {
        return when (this) {
            is Success<*> -> "Success[Response=$value]"
            is GenericError -> "GenericError[Error=${throwable?.message}]"
            NetworkError -> "NetworkError"
        }
    }
}

inline fun <reified T> ApiResult<T>.doIfNetworkError(callback: () -> Unit) {
    if (this is ApiResult.NetworkError) {
        callback()
    }
}

inline fun <reified T> ApiResult<T>.doIfGenericError(
    callback: (throwable: Throwable?) -> Unit
) {
    if (this is ApiResult.GenericError) {
        callback(throwable)
    }
}

inline fun <reified T> ApiResult<T>.doIfSuccess(callback: (value: T) -> Unit) {
    if (this is ApiResult.Success) {
        callback(value)
    }
}

inline fun <reified T> List<ApiResult<T>>.doIfAnyNetworkError(
    callback: (result: ApiResult.NetworkError) -> Unit
) {
    val index = indexOfFirst { it is ApiResult.NetworkError }
    if (index >= 0) {
        callback(filterIsInstance<ApiResult.NetworkError>().first())
    }
}

inline fun <reified T> List<ApiResult<T>>.doIfAnyGenericError(
    callback: (result: ApiResult.GenericError) -> Unit
) {
    val index = indexOfFirst { it is ApiResult.GenericError }
    if (index >= 0) {
        callback(filterIsInstance<ApiResult.GenericError>().first())
    }
}
