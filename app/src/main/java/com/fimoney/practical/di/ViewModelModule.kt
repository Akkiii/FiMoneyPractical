package com.fimoney.practical.di

import com.fimoney.practical.ui.bookmark.BookMarkViewModel
import com.fimoney.practical.ui.home.HomeViewModel
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module

val viewModelModule = module {
    viewModel {
        HomeViewModel(get())
    }

    viewModel {
        BookMarkViewModel(get())
    }
}