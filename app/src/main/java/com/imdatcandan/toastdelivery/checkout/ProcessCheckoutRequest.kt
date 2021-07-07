package com.imdatcandan.toastdelivery.checkout

import com.google.gson.annotations.SerializedName

data class ProcessCheckoutRequest(
    @SerializedName("payment_type") val paymentType: String,
    @SerializedName("card") val creditCard: CreditCard
)
