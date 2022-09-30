package com.fimoney.practical.data.remote

import com.fimoney.practical.extension.params
import com.fimoney.practical.extension.toFromBody
import okhttp3.FormBody
import okhttp3.Interceptor
import okhttp3.Response

class FiMoneyInterceptor : Interceptor {

    override fun intercept(chain: Interceptor.Chain): Response {
        val request = chain.request()
        val requestBuilder = request.newBuilder()

        val body = request.body as? FormBody
        if (body != null) {
            val params = buildMap<String, String> {
                putAll(body.params())
            }
            requestBuilder.post(params.toFromBody())
        }

        return chain.proceed(requestBuilder.build())
    }
}
