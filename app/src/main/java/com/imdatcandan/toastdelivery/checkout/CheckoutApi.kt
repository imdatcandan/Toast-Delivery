package com.imdatcandan.toastdelivery.checkout

import retrofit2.Response
import retrofit2.http.*

interface CheckoutApi {
    @Headers("Content-Type: application/json")
    @POST("/v0.1/checkouts")
    suspend fun createCheckout(@Body body: CreateCheckoutRequest): Response<CreateCheckoutResponse>

    @Headers("Content-Type: application/json")
    @PUT("/v0.1/checkouts/{id}")
    suspend fun processCheckout(
        @Path("id") id: String,
        @Body body: ProcessCheckoutRequest
    ): Response<ProcessCheckoutResponse>
}
