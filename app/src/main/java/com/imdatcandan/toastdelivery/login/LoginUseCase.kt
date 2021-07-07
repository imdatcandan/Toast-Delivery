package com.imdatcandan.toastdelivery.login

import android.content.SharedPreferences
import android.util.Log
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import com.imdatcandan.toastdelivery.BuildConfig
import com.imdatcandan.toastdelivery.checkout.ErrorResponse
import com.imdatcandan.toastdelivery.view.ViewState
import kotlinx.coroutines.*
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow

class LoginUseCase(
    private val loginRepository: LoginRepository,
    private val preferences: SharedPreferences,
    private val gson: Gson,
    private val externalScope: CoroutineScope = GlobalScope,
    private val dispatcher: CoroutineDispatcher = Dispatchers.Main
) {

    private val _uiState = MutableStateFlow<ViewState>(ViewState.Loading(false))
    val uiState: StateFlow<ViewState> = _uiState

    private val type = object : TypeToken<ErrorResponse>() {}.type

    fun doLogin() {
        _uiState.value = ViewState.Loading(true)
        externalScope.launch(dispatcher) {
            try {
                val response = loginRepository.getToken(
                    BuildConfig.GRANT_TYPE,
                    BuildConfig.CLIENT_ID,
                    BuildConfig.CLIENT_SECRET
                )
                if (response.isSuccessful && response.body() != null) {
                    val accessToken = response.body()?.accessToken
                    Log.d("Login", "accessToken=$accessToken")
                    preferences.edit().putString(ACCESS_TOKEN, accessToken).apply()
                    _uiState.value = ViewState.Success("Token request successful")
                } else {
                    val errorResponse: ErrorResponse? =
                        gson.fromJson(response.errorBody()?.charStream(), type)
                    _uiState.value =
                        ViewState.Error("Token request unsuccessful ${errorResponse?.message}")
                }
            } catch (exception: Exception) {
                _uiState.value =
                    ViewState.Error("Failed to get token ${exception.localizedMessage}")
            } finally {
                _uiState.value = ViewState.Loading(false)
            }
        }
    }

    companion object {
        const val ACCESS_TOKEN = "ACCESS_TOKEN"
    }
}
