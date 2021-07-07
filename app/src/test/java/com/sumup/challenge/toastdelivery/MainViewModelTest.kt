package com.imdatcandan.toastdelivery

import com.imdatcandan.toastdelivery.checkout.CheckoutUseCase
import com.imdatcandan.toastdelivery.checkout.CreditCard
import com.imdatcandan.toastdelivery.login.LoginUseCase
import com.imdatcandan.toastdelivery.viewmodel.MainViewModel
import io.mockk.mockk
import io.mockk.verify
import org.junit.Test
import kotlin.test.assertEquals

class MainViewModelTest : BaseUnitTest<MainViewModel>() {

    private val loginUseCase: LoginUseCase = mockk(relaxed = true)
    private val checkoutUseCase: CheckoutUseCase = mockk(relaxed = true)
    private val creditCard: CreditCard = mockk(relaxed = true)

    override fun initSelf() = MainViewModel(loginUseCase, checkoutUseCase)

    @Test
    fun `WHEN login called THEN verify doLogin called`() {
        tested.login()

        verify {
            loginUseCase.doLogin()
        }
    }

    @Test
    fun `WHEN purchase called THEN verify createTransaction called`() {
        tested.purchase(AMOUNT)

        verify {
            checkoutUseCase.createTransaction(AMOUNT)
        }
    }

    @Test
    fun `WHEN pay called THEN verify processCheckout called`() {
        tested.pay(creditCard)

        verify {
            checkoutUseCase.processCheckout(creditCard)
        }
    }

    @Test
    fun `WHEN getLoginUiState called THEN assert loginUiState`() {
        val result = tested.getLoginUiState()

        assertEquals(result, loginUseCase.uiState)
    }

    @Test
    fun `WHEN getCheckoutUiState called THEN assert checkoutUiState`() {

        val result = tested.getCheckoutUiState()

        assertEquals(result, checkoutUseCase.uiState)
    }

    private companion object {
        private const val AMOUNT = "10.99"
    }

}