package com.fimoney.practical.utils.initializer

import android.content.Context
import androidx.startup.Initializer
import com.fimoney.practical.di.initKoin

class KoinInitializer : Initializer<Unit> {
    override fun create(context: Context) {
        initKoin(context)
    }

    override fun dependencies(): List<Class<out Initializer<*>>> {
        return listOf(TimberInitializer::class.java)
    }
}
