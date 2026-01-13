package com.example.ghostai.data.network

import java.io.IOException

sealed class ResultWrapper<out T> {
    data class Success<out T>(val value: T) : ResultWrapper<T>()

    data class ApiError(val error: MutableList<Error>) : ResultWrapper<Nothing>()

    data class GenericError(
        val code: Int,
        val message: String,
        val codeString: String
    ) : ResultWrapper<Nothing>()

    data class NetworkError(val io: IOException) : ResultWrapper<Nothing>()

    object TimeOutError : ResultWrapper<Nothing>()

    object NullResponseError : ResultWrapper<Nothing>()

    object ExceptionResponseError : ResultWrapper<Nothing>()
}
