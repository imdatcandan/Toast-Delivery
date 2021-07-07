package com.imdatcandan.toastdelivery.view

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import com.imdatcandan.toastdelivery.checkout.CreditCard
import com.imdatcandan.toastdelivery.databinding.ActivityPaymentBinding
import com.imdatcandan.toastdelivery.viewmodel.MainViewModel
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch
import org.koin.androidx.viewmodel.ext.android.viewModel

class PaymentActivity : AppCompatActivity() {

    private val viewModel: MainViewModel by viewModel()
    private lateinit var binding: ActivityPaymentBinding
    private lateinit var creditCard: CreditCard

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityPaymentBinding.inflate(layoutInflater)
        setContentView(binding.root)

        lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.STARTED) {
                viewModel.getCheckoutUiState().collect {
                    when (it) {
                        is ViewState.Success -> showSuccessMessage(it.message)
                        is ViewState.Error -> showErrorDialog(it.message) { _, _ ->
                            viewModel.pay(creditCard)
                        }
                        is ViewState.Loading -> binding.progressBar.showLoading(it.isLoading)
                    }
                }
            }
        }

        binding.btnPay.setOnClickListener {
            creditCard = CreditCard(
                name = binding.creditCardFlow.creditCardHolder(),
                number = binding.creditCardFlow.creditCardNumberWithoutNotDigits(),
                expiryYear = binding.creditCardFlow.creditCardExpiryDate().substring(
                    EXPIRY_YEAR_START_POSITION, EXPIRY_YEAR_END_POSITION
                ),
                expiryMonth = binding.creditCardFlow.creditCardExpiryDate().substring(
                    EXPIRY_MONTH_START_POSITION, EXPIRY_MONTH_END_POSITION
                ),
                cvv = binding.creditCardFlow.creditCardCvvCode()
            )
            viewModel.pay(creditCard)
        }
    }

    private companion object {
        private const val EXPIRY_MONTH_START_POSITION = 0
        private const val EXPIRY_MONTH_END_POSITION = 2
        private const val EXPIRY_YEAR_START_POSITION = 3
        private const val EXPIRY_YEAR_END_POSITION = 5
    }
}
