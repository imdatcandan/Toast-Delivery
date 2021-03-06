package com.imdatcandan.toastdelivery.view

import com.google.gson.annotations.SerializedName

data class ToastItem(
    val name: String,
    val price: String,
    @SerializedName("image_name") val imageName: String
)
