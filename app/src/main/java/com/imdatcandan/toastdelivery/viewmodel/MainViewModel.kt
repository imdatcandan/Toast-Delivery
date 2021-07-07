package com.imdatcandan.toastdelivery.viewmodel

import androidx.lifecycle.ViewModel
import com.imdatcandan.toastdelivery.checkout.CheckoutUseCase
import com.imdatcandan.toastdelivery.checkout.CreditCard
import com.imdatcandan.toastdelivery.login.LoginUseCase

class MainViewModel(
    private val loginUseCase: LoginUseCase,
    private val checkoutUseCase: CheckoutUseCase
) : ViewModel() {

    fun login() {
        loginUseCase.doLogin()
    }

    fun purchase(amount: String) {
        checkoutUseCase.createTransaction(amount)
    }

    fun pay(creditCard: CreditCard) {
        checkoutUseCase.processCheckout(creditCard)
    }

    fun getLoginUiState() = loginUseCase.uiState

    fun getCheckoutUiState() = checkoutUseCase.uiState
}