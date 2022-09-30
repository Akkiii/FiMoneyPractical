package com.fimoney.practical.di

import android.content.Context
import com.fimoney.practical.BuildConfig
import org.koin.android.ext.koin.androidContext
import org.koin.android.ext.koin.androidLogger
import org.koin.core.component.KoinComponent
import org.koin.core.component.inject
import org.koin.core.context.startKoin
import org.koin.core.logger.Level

fun initKoin(context: Context) {
    startKoin {
        androidContext(context)
        if (BuildConfig.DEBUG) {
            androidLogger(Level.DEBUG)
        }
        modules(
            listOf(
                appModule,
                networkModule,
                viewModelModule,
                repoModule
            )
        )
    }
}

inline fun <reified T> getKoinInstance() =
    object : KoinComponent {
        val value: T by inject()
    }.value
