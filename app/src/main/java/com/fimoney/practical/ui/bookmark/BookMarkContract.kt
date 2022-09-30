package com.fimoney.practical.ui.bookmark

import com.fimoney.practical.ui.base.UiEffect
import com.fimoney.practical.ui.base.UiEvent
import com.fimoney.practical.ui.base.UiState

sealed class BookMarkUIEvent : UiEvent {

}

sealed class BookMarkUIState : UiState {
    object Initial : BookMarkUIState()
}

sealed class BookMarkUIEffect : UiEffect {

}