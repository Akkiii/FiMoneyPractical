package com.fimoney.practical.di

import com.fimoney.practical.data.respository.DefaultFiMoneyRepository
import com.fimoney.practical.data.respository.FiMoneyRepository
import org.koin.dsl.module

val repoModule = module {
    single<FiMoneyRepository> {
        DefaultFiMoneyRepository(get(),get())
    }
}
