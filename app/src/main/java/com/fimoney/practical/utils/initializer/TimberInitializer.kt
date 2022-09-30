package com.fimoney.practical.utils.initializer

import android.content.Context
import androidx.startup.Initializer
import com.fimoney.practical.BuildConfig
import timber.log.Timber

class TimberInitializer : Initializer<Unit> {
    override fun create(context: Context) {
        if (BuildConfig.DEBUG) {
            Timber.plant(object : Timber.DebugTree() {
                override fun createStackElementTag(element: StackTraceElement): String {
                    return "FI_" + super.createStackElementTag(element)
                }
            })
        }
    }

    override fun dependencies(): List<Class<out Initializer<*>>> {
        return emptyList()
    }
}
