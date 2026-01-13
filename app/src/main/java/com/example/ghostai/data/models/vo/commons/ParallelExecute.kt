package com.example.ghostai.data.models.vo.commons

import com.example.ghostai.data.network.ResultWrapper
import kotlinx.coroutines.flow.Flow

sealed class ParallelExecute<out T> {
    data class RequestSuspend<T>(
        val request: suspend () -> ResultWrapper<T>,
        val onSuccess: (T) -> Unit = {},
        val onError: ((Throwable) -> Unit)? = null
    ) : ParallelExecute<T>()

    data class RequestFlow<T>(
        val requestFlow: Flow<ResultWrapper<T>>,
        val onSuccess: (T) -> Unit = {},
        val onError: ((Throwable) -> Unit)? = null,
        val onComplete: (() -> Unit)? = null
    ) : ParallelExecute<T>()
}