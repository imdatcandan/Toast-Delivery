package com.imdatcandan.toastdelivery.checkout

import com.google.gson.annotations.SerializedName

data class ErrorResponse(
    @SerializedName("error_code") val errorCode: String,
    @SerializedName("message") val message: String,
    @SerializedName("param") val param: String,
)
