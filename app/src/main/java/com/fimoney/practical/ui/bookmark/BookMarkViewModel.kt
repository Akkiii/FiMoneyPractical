package com.fimoney.practical.ui.bookmark

import androidx.lifecycle.viewModelScope
import com.fimoney.practical.data.respository.FiMoneyRepository
import com.fimoney.practical.model.DataModel
import com.fimoney.practical.ui.base.BaseViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class BookMarkViewModel(private val repository: FiMoneyRepository) :
    BaseViewModel<BookMarkUIEvent, BookMarkUIState, BookMarkUIEffect>() {
    private val _recentBookMark = MutableStateFlow<List<DataModel>>(emptyList())
    val recentBookMark = _recentBookMark.asStateFlow()

    init {
        viewModelScope.launch {
            repository.getBookMark().collect { results ->
                _recentBookMark.value = results
            }
        }
    }

    override fun createInitialState(): BookMarkUIState = BookMarkUIState.Initial

    override fun handleEvent(event: BookMarkUIEvent) {

    }
}