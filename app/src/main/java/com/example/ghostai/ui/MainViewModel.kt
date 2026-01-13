package com.example.ghostai.ui

import com.example.ghostai.data.models.vo.commons.UiState
import com.example.ghostai.ui.base.BaseViewModel
import com.example.ghostai.ui.base.MainEffect
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class MainViewModel @Inject constructor() : BaseViewModel<Nothing, MainEffect>() {

    override val initialState: UiState<Nothing>
        get() = UiState.Init

    override fun errorState(message: String): UiState<Nothing>? {
        return null
    }

    fun demo() {
        updateState { UiState.Loading }
    }
}