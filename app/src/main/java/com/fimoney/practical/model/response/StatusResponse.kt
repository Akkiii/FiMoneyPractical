package com.fimoney.practical.model.response

import com.squareup.moshi.Json

abstract class StatusResponse {

    @Json(name = "Response")
    var status: String = ""

    @Json(name = "Error")
    var message: String = ""

    override fun toString(): String {
        return "BaseResponse(Response=$status, Error=$message)"
    }
}
