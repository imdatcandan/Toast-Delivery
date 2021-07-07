package com.imdatcandan.toastdelivery.login

import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import retrofit2.Response

class LoginRepository(
    private val loginApi: LoginApi,
    private val ioDispatcher: CoroutineDispatcher = Dispatchers.IO
) {
    suspend fun getToken(
        grantType: String,
        clientId: String,
        clientSecret: String
    ): Response<TokenResponse> =
        withContext(ioDispatcher) {
            return@withContext loginApi.getToken(grantType, clientId, clientSecret)
        }
}