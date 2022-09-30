package com.fimoney.practical.di

import android.annotation.SuppressLint
import android.content.Context
import android.util.Log
import com.fimoney.practical.BuildConfig
import com.fimoney.practical.data.StatusResponseConverterFactory
import com.fimoney.practical.data.remote.FiMoneyApiService
import com.fimoney.practical.data.remote.FiMoneyInterceptor
import com.squareup.moshi.Moshi
import okhttp3.ConnectionSpec
import okhttp3.OkHttpClient
import okhttp3.TlsVersion
import okhttp3.logging.HttpLoggingInterceptor
import org.koin.dsl.module
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory
import java.util.concurrent.TimeUnit

private const val READ_TIMEOUT_SEC = 60L
private const val CONNECT_TIMEOUT_SEC = 60L

val networkModule = module {
    fun provideBaseOkHttp(context: Context): OkHttpClient {
        val connectionSpec = ConnectionSpec.Builder(ConnectionSpec.MODERN_TLS)
            .tlsVersions(TlsVersion.TLS_1_2)
            .allEnabledCipherSuites()
            .build()

        return OkHttpClient.Builder()
            .readTimeout(READ_TIMEOUT_SEC, TimeUnit.SECONDS)
            .connectTimeout(CONNECT_TIMEOUT_SEC, TimeUnit.SECONDS)
            .connectionSpecs(listOf(connectionSpec))
            .build()
    }

    fun provideApiService(
        okHttpClient: OkHttpClient,
        moshi: Moshi
    ): FiMoneyApiService {
        val client = okHttpClient.newBuilder()
            .addFiMoneyInterceptor()
            .addLoggingInterceptor("FiMoneyApi")
            .build()

        val retrofit = Retrofit.Builder()
            .client(client)
            .baseUrl(BuildConfig.SERVICE_URL)
            .addConverterFactory(StatusResponseConverterFactory())
            .addConverterFactory(MoshiConverterFactory.create(moshi).asLenient())
            .build()

        return retrofit.create(FiMoneyApiService::class.java)
    }

    single {
        provideBaseOkHttp(get())
    }

    single {
        provideApiService(get(), get())
    }
}

private fun OkHttpClient.Builder.addFiMoneyInterceptor(): OkHttpClient.Builder {
    addInterceptor(FiMoneyInterceptor())
    return this
}


@SuppressLint("LogNotTimber")
private fun OkHttpClient.Builder.addLoggingInterceptor(tag: String): OkHttpClient.Builder {
    val loggingInterceptor =
        HttpLoggingInterceptor { message -> Log.d(tag, message) }.apply {
            level = if (BuildConfig.DEBUG) {
                HttpLoggingInterceptor.Level.BODY
            } else {
                HttpLoggingInterceptor.Level.NONE
            }
        }
    addInterceptor(loggingInterceptor)

    return this
}
