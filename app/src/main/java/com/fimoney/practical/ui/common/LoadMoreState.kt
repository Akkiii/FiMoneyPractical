package com.fimoney.practical.ui.common

sealed class LoadMoreState {
    object NotLoading : LoadMoreState()
    object Loading : LoadMoreState()
}
