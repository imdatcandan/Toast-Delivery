package com.imdatcandan.toastdelivery.checkout

import com.google.gson.annotations.SerializedName

data class CreditCard(
    @SerializedName("name") val name: String,
    @SerializedName("number") val number: String,
    @SerializedName("expiry_year") val expiryYear: String,
    @SerializedName("expiry_month") val expiryMonth: String,
    @SerializedName("cvv") val cvv: String
)