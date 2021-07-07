package com.imdatcandan.toastdelivery.checkout

import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import retrofit2.Response

class CheckoutRepository(
    private val checkoutApi: CheckoutApi,
    private val ioDispatcher: CoroutineDispatcher = Dispatchers.IO
) {
    suspend fun createCheckout(createCheckoutRequest: CreateCheckoutRequest): Response<CreateCheckoutResponse> =
        withContext(ioDispatcher) {
            return@withContext checkoutApi.createCheckout(createCheckoutRequest)
        }

    suspend fun processCheckout(
        id: String,
        processCheckoutRequest: ProcessCheckoutRequest
    ): Response<ProcessCheckoutResponse> =
        withContext(ioDispatcher) {
            return@withContext checkoutApi.processCheckout(id, processCheckoutRequest)
        }
}