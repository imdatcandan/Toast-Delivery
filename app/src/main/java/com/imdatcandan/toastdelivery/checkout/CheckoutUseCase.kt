package com.imdatcandan.toastdelivery.checkout

import android.util.Log
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import com.imdatcandan.toastdelivery.BuildConfig
import com.imdatcandan.toastdelivery.view.ViewState
import kotlinx.coroutines.*
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import java.util.*

class CheckoutUseCase(
    private val checkoutRepository: CheckoutRepository,
    private val gson: Gson,
    private val externalScope: CoroutineScope = GlobalScope,
    private val dispatcher: CoroutineDispatcher = Dispatchers.Main
) {
    private var checkoutId: String = ""

    private val _uiState = MutableStateFlow<ViewState>(ViewState.Loading(false))
    val uiState: StateFlow<ViewState> = _uiState

    private val type = object : TypeToken<ErrorResponse>() {}.type

    fun createTransaction(amount: String) {
        _uiState.value = ViewState.Loading(true)
        val checkoutReference = UUID.randomUUID().toString()
        val checkoutRequest =
            CreateCheckoutRequest(
                checkoutReference,
                amount.toBigDecimal(),
                DEFAULT_CURRENCY,
                BuildConfig.MERCHANT_CODE
            )

        externalScope.launch(dispatcher) {
            try {
                val response = checkoutRepository.createCheckout(checkoutRequest)
                if (response.isSuccessful && response.body() != null) {
                    checkoutId = response.body()?.id ?: ""
                    _uiState.value = ViewState.Success("Successfully created checkout")
                } else {
                    val errorResponse: ErrorResponse? =
                        gson.fromJson(response.errorBody()?.charStream(), type)
                    _uiState.value =
                        ViewState.Error("Error creating checkout: ${errorResponse?.message}")
                }
            } catch (exception: Exception) {
                _uiState.value =
                    ViewState.Error("Failed to create checkout: ${exception.localizedMessage}")
            } finally {
                _uiState.value = ViewState.Loading(false)
            }
        }
    }

    fun processCheckout(creditCard: CreditCard) {
        _uiState.value = ViewState.Loading(true)
        val processCheckoutRequest = ProcessCheckoutRequest(DEFAULT_PAYMENT_TYPE, creditCard)
        Log.d("CreditCard", creditCard.toString())

        externalScope.launch(dispatcher) {
            try {
                val response =
                    checkoutRepository.processCheckout(checkoutId, processCheckoutRequest)
                if (response.isSuccessful && response.body() != null) {
                    _uiState.value = ViewState.Success("Successfully payed")
                } else {
                    val errorResponse: ErrorResponse? =
                        gson.fromJson(response.errorBody()?.charStream(), type)
                    _uiState.value =
                        ViewState.Error("Error on paying: ${errorResponse?.message}")
                }
            } catch (exception: Exception) {
                _uiState.value = ViewState.Error("Failed to pay: $exception.localizedMessage")
            } finally {
                _uiState.value = ViewState.Loading(false)
            }
        }
    }

    private companion object {
        private const val DEFAULT_CURRENCY = "EUR"
        private const val DEFAULT_PAYMENT_TYPE = "card"
    }
}