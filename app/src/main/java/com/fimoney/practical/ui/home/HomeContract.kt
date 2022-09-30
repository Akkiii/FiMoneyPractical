package com.fimoney.practical.ui.home

import com.fimoney.practical.model.DataModel
import com.fimoney.practical.model.response.MovieDetailResponse
import com.fimoney.practical.ui.base.UiEffect
import com.fimoney.practical.ui.base.UiEvent
import com.fimoney.practical.ui.base.UiState

sealed class HomeUIEvent : UiEvent {
    data class LoadData(val type: String) : HomeUIEvent()
    data class LoadMoreData(val type: String) : HomeUIEvent()
    data class LoadMovieDetail(val id: String) : HomeUIEvent()
    data class AddToBookMark(val pos : Int) : HomeUIEvent()
    data class RemoveFromBookMark(val pos : Int) : HomeUIEvent()
}

sealed class HomeUIState : UiState {
    object Initial : HomeUIState()
    data class Loading(val isLoading: Boolean) : HomeUIState()
    data class Loaded(val dataList: List<DataModel>) : HomeUIState()
    data class ShowProgress(val isLoading: Boolean) : HomeUIState()
}

sealed class HomeUIEffect : UiEffect {
    data class ShowError(val message: String) : HomeUIEffect()
    object ShowNetworkError : HomeUIEffect()
    data class MovieDetailError(val message: String) : HomeUIEffect()
    object ShowMovieNetworkError : HomeUIEffect()
    data class MovieDetail(val detail: MovieDetailResponse) : HomeUIEffect()
}