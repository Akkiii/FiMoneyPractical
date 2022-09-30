package com.fimoney.practical.di

import android.app.Application
import com.fimoney.practical.db.AppDatabase
import com.squareup.moshi.Moshi
import org.koin.dsl.module

val appModule = module {

    fun provideMoshi() = Moshi.Builder().build()

    fun provideAppDatabase(application: Application) = AppDatabase.create(application)

    fun provideBookMarkDao(appDatabase: AppDatabase) = appDatabase.bookmarkDao()

    single {
        provideAppDatabase(get())
    }

    single {
        provideBookMarkDao(get())
    }

    single {
        provideMoshi()
    }
}
