package com.imdatcandan.toastdelivery.login

import android.content.SharedPreferences
import com.imdatcandan.toastdelivery.login.LoginUseCase.Companion.ACCESS_TOKEN
import okhttp3.Interceptor
import okhttp3.Request
import okhttp3.Response

class TokenInterceptor(private val preferences: SharedPreferences) : Interceptor {

    override fun intercept(chain: Interceptor.Chain): Response {
        val accessToken = preferences.getString(ACCESS_TOKEN, "")
        val newRequest: Request = chain.request().newBuilder()
            .header("Authorization", "Bearer $accessToken")
            .build()
        return chain.proceed(newRequest)
    }
}