package com.fimoney.practical.ui.home

import androidx.lifecycle.viewModelScope
import com.fimoney.practical.data.doIfGenericError
import com.fimoney.practical.data.doIfNetworkError
import com.fimoney.practical.data.doIfSuccess
import com.fimoney.practical.data.respository.FiMoneyRepository
import com.fimoney.practical.extension.clearAndAddAll
import com.fimoney.practical.model.DataModel
import com.fimoney.practical.ui.base.BaseViewModel
import com.fimoney.practical.ui.base.InputField
import com.fimoney.practical.ui.common.LoadMoreState
import com.fimoney.practical.utils.Constant
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.filterNotNull
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.launch

class HomeViewModel(private val repository: FiMoneyRepository) :
    BaseViewModel<HomeUIEvent, HomeUIState, HomeUIEffect>() {
    override fun createInitialState(): HomeUIState = HomeUIState.Initial

    private var totalCount = 0
    private var pageCount = 1
    private val dataList = mutableListOf<DataModel>()

    var hasMore = true
        private set

    private val _loadMoreMoreState: MutableStateFlow<LoadMoreState> =
        MutableStateFlow(LoadMoreState.NotLoading)
    val loadMoreState = _loadMoreMoreState.asStateFlow()

    val queryField: InputField<String> = InputField(initialValue = "Avenger") { s ->
        return@InputField s?.length!! > 1
    }

    override fun handleEvent(event: HomeUIEvent) {
        when (event) {
            is HomeUIEvent.LoadData -> {
                getData(true, event.type)
            }

            is HomeUIEvent.LoadMoreData -> {
                getData(false, type = event.type)
            }

            is HomeUIEvent.LoadMovieDetail -> {
                getMovieDetail(event.id)
            }

            is HomeUIEvent.AddToBookMark -> {
                addToBookMark(event.pos)
            }

            is HomeUIEvent.RemoveFromBookMark -> {
                removeFromBookMark(event.pos)
            }
        }
    }

    private fun addToBookMark(pos: Int) {
        viewModelScope.launch {
            dataList[pos].isBookMark = true
            repository.addToBookMark(dataList[pos])
        }
    }

    private fun removeFromBookMark(pos: Int) {
        viewModelScope.launch(Dispatchers.IO) {
            dataList[pos].isBookMark = false
            repository.removeBookMark(dataList[pos])
        }
    }

    private fun getData(isInitialLoad: Boolean, type: String) {
        viewModelScope.launch {
            if (isInitialLoad) {
                totalCount = 0
                pageCount = 1
                dataList.clear()
                hasMore = true

                setState {
                    HomeUIState.Loading(true)
                }
            } else {
                pageCount++
                _loadMoreMoreState.value = LoadMoreState.Loading
            }

            val map = HashMap<String, String>()
            map["apikey"] = Constant.API_KEY
            map["page"] = "$pageCount"
            map["s"] = queryField.flow.value?.trim().orEmpty()
            map["type"] = type
            val result = repository.getData(map)

            if (isInitialLoad) {
                setState {
                    HomeUIState.Loading(isLoading = false)
                }
            } else {
                _loadMoreMoreState.value = LoadMoreState.NotLoading
            }

            result.doIfSuccess { response ->
                if (response.dataList.size >= response.totalResult.toInt()) {
                    hasMore = false
                }

                if (isInitialLoad) {
                    dataList.clearAndAddAll {
                        response.dataList
                    }
                    totalCount = response.totalResult.toInt()
                } else {
                    dataList.addAll(response.dataList)
                }

                setState {
                    HomeUIState.Loaded(dataList = dataList.toList())
                }
            }

            result.doIfGenericError {
                setEffect {
                    HomeUIEffect.ShowError(it?.message.orEmpty())
                }
            }

            result.doIfNetworkError {
                setEffect {
                    HomeUIEffect.ShowNetworkError
                }
            }
        }
    }

    private fun getMovieDetail(id: String) {
        viewModelScope.launch {
            val map = HashMap<String, String>()
            map["apikey"] = Constant.API_KEY
            map["i"] = id
            map["plot"] = Constant.PLOT_FULL
            setState {
                HomeUIState.ShowProgress(true)
            }
            val result = repository.getMovieDetail(map)

            setState {
                HomeUIState.ShowProgress(false)
            }
            result.doIfSuccess {
                setEffect {
                    HomeUIEffect.MovieDetail(it)
                }
            }

            result.doIfGenericError {
                setEffect {
                    HomeUIEffect.MovieDetailError(it?.message.orEmpty())
                }
            }

            result.doIfNetworkError {
                setEffect {
                    HomeUIEffect.ShowMovieNetworkError
                }
            }
        }
    }
}