package com.example.ghostai.data.datasources

import com.example.ghostai.data.network.ResultWrapper
import com.example.ghostai.utils.extentions.toString

abstract class BaseDataSource {

    protected suspend fun <T> safeApiCall(apiCall: suspend () -> T): ResultWrapper<T> {
        return try {
            val response = apiCall()
            ResultWrapper.Success(response)
        } catch (e: Exception) {
            ResultWrapper.AppError(e.message.toString())
        }
    }
}