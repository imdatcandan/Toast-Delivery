package com.imdatcandan.toastdelivery.checkout

import com.google.gson.annotations.SerializedName
import java.math.BigDecimal

data class CreateCheckoutResponse(
    @SerializedName("checkout_reference") val reference: String,
    @SerializedName("amount") val amount: BigDecimal,
    @SerializedName("currency") val currencyCode: String,
    @SerializedName("merchant_code") val merchantCode: String,
    @SerializedName("id") val id: String,
    @SerializedName("status") val status: String,
    @SerializedName("date") val date: String
)

