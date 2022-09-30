package com.fimoney.practical.data

import com.fimoney.practical.model.response.StatusResponse
import com.squareup.moshi.JsonReader
import okhttp3.ResponseBody
import okhttp3.ResponseBody.Companion.asResponseBody
import retrofit2.Converter
import retrofit2.Retrofit
import timber.log.Timber
import java.io.IOException
import java.lang.reflect.Type

class InvalidStatusException(message: String) : Exception(message)

class StatusResponseConverterFactory : Converter.Factory() {

    companion object {
        private val options = JsonReader.Options.of("Response", "Error")
    }

    override fun responseBodyConverter(
        type: Type,
        annotations: Array<out Annotation>,
        retrofit: Retrofit
    ): Converter<ResponseBody, *>? {
        if (getRawType(type).superclass != StatusResponse::class.java) return null
        val delegateConverter = retrofit.nextResponseBodyConverter<Any>(this, type, annotations)
        return StatusConverter(delegateConverter)
    }

    inner class StatusConverter(
        private val delegate: Converter<ResponseBody, Any>
    ) : Converter<ResponseBody, Any> {
        override fun convert(value: ResponseBody): Any? {
            var status :String?=null
            var message: String? = null

            try {
                JsonReader.of(value.copy().source()).apply {
                    beginObject()
                    while (hasNext()) {
                        when (selectName(options)) {
                            0 -> status = nextString()
                            1 -> message = nextString()
                            else -> {
                                skipName()
                                skipValue()
                            }
                        }
                    }
                    endObject()
                }
            } catch (t: Throwable) {
                Timber.d(t, "ResponseBody parsing failed.")
            }

            if (status == "True") {
                return delegate.convert(value)
            }

            throw InvalidStatusException(
                message = message ?: value.copy().string()
            )
        }
    }
}

@Throws(IOException::class)
private fun ResponseBody.copy(): ResponseBody {
    val source = source()
    source.request(Long.MAX_VALUE)
    val bufferedCopy = source.buffer.clone()
    return bufferedCopy.asResponseBody(contentType(), contentLength())
}
