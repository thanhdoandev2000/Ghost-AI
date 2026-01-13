package com.example.ghostai.ui.base

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.ghostai.data.models.vo.commons.ParallelExecute
import com.example.ghostai.data.models.vo.commons.UiState
import com.example.ghostai.data.network.ResultWrapper
import kotlinx.coroutines.CoroutineExceptionHandler
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Deferred
import kotlinx.coroutines.async
import kotlinx.coroutines.awaitAll
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import kotlinx.coroutines.supervisorScope

sealed class MainEffect

abstract class BaseViewModel<S, E> : ViewModel() {

    abstract val initialState: UiState<S>
    private val _uiState by lazy { MutableStateFlow(initialState) }
    internal val uiState by lazy { _uiState.asStateFlow() }
    private val _effect = Channel<E>(Channel.BUFFERED)
    internal val effect = _effect.receiveAsFlow()

    protected fun updateState(reducer: UiState<S>.() -> UiState<S>) {
        _uiState.update { it.reducer() }
    }

    protected fun sendEffect(effect: E) {
        viewModelScope.launch { _effect.send(effect) }
    }

    protected val handler = CoroutineExceptionHandler { _, exception ->
        updateState { UiState.Error(exception.message ?: "Unknown error") }
    }

    protected open fun errorState(message: String): UiState<S>? {
        return UiState.Error(message)
    }

    protected open fun hideLoading(): UiState<S>? {
        return UiState.FinishLoading
    }

    protected fun <T> execute(
        request: suspend () -> ResultWrapper<T>,
        isLoading: Boolean = true,
        onSuccess: (T) -> Unit,
        onError: (Throwable) -> Unit = {}
    ) {
        viewModelScope.launch(handler) {
            if (isLoading) updateState { UiState.Loading }
            val result = request()
            result.handleResult(onSuccess, onError)
        }
    }

    protected fun <T> collectFlow(
        request: Flow<ResultWrapper<T>>,
        isLoading: Boolean = true,
        onSuccess: (T) -> Unit,
        onError: ((Throwable) -> Unit)? = null
    ) {
        viewModelScope.launch(handler) {
            if (isLoading) updateState { UiState.Loading }
            request.collect { result ->
                result.handleResult(onSuccess, onError)
            }
        }
    }

    private suspend fun <T> ParallelExecute<T>.execute(scope: CoroutineScope) {
        when (this) {
            is ParallelExecute.RequestSuspend -> {
                request().handleResult(onSuccess, onError)
            }

            is ParallelExecute.RequestFlow -> {
                scope.launch {
                    requestFlow
                        .catch { e -> onError?.invoke(e) }
                        .collect { result ->
                            result.handleResult(onSuccess, onError)
                        }
                    onComplete?.invoke()
                }
            }
        }
    }

    protected fun executeApiOnThreads(
        vararg executes: ParallelExecute<*>,
        isLoading: Boolean = true,
        onAllFinished: () -> Unit = {}
    ) {
        viewModelScope.launch(handler) {
            if (isLoading) updateState { UiState.Loading }

            supervisorScope {
                val suspendTasks = executes.filterIsInstance<ParallelExecute.RequestSuspend<*>>()
                val flowTasks = executes.filterIsInstance<ParallelExecute.RequestFlow<*>>()
                flowTasks.forEach { it.execute(this) }
                suspendTasks.map { async { it.execute(this) } }.awaitAll()
            }

            onAllFinished()
            if (isLoading) hideLoading()?.let { updateState { it } }
        }
    }

    private fun <T> ResultWrapper<T>.handleResult(
        onSuccess: (T) -> Unit,
        onError: ((Throwable) -> Unit)?
    ) {
        when (this) {
            is ResultWrapper.Success -> onSuccess(value)

            else -> {
                val (message, throwable) = when (this) {
                    is ResultWrapper.NetworkError -> io.message to io
                    is ResultWrapper.ApiError -> error.toString() to Throwable(error.toString())
                    is ResultWrapper.GenericError -> message to Throwable(message)
                    else -> toString() to Throwable(this.toString())
                }

                errorState(message.toString())?.let { newState ->
                    updateState { newState }
                }

                onError?.invoke(throwable)
            }
        }
    }
}