package com.imdatcandan.toastdelivery.view

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.recyclerview.widget.GridLayoutManager
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import com.imdatcandan.toastdelivery.databinding.ActivityShopBinding
import com.imdatcandan.toastdelivery.util.getJsonDataFromAsset
import com.imdatcandan.toastdelivery.viewmodel.MainViewModel
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch
import org.koin.androidx.viewmodel.ext.android.viewModel

class ShopActivity : AppCompatActivity(), ToastItemClickListener {

    private val viewModel: MainViewModel by viewModel()
    private lateinit var binding: ActivityShopBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityShopBinding.inflate(layoutInflater)
        setContentView(binding.root)
        viewModel.login()
        setUpRecyclerView()

        lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.STARTED) {
                viewModel.getLoginUiState().collect {
                    when (it) {
                        is ViewState.Success -> showSuccessMessage(it.message)
                        is ViewState.Error -> showErrorDialog(it.message) { _, _ ->
                            viewModel.login()
                        }
                        is ViewState.Loading -> binding.progressBar.showLoading(it.isLoading)
                    }
                }
            }
        }

        binding.btnPurchase.setOnClickListener {
            viewModel.purchase(binding.cartSelectedItemPrice.text.toString())
            startActivity(Intent(this, PaymentActivity::class.java))
        }
    }

    private fun setUpRecyclerView() {
        binding.recyclerView.layoutManager = GridLayoutManager(this, 2)
        binding.recyclerView.addItemDecoration(
            SpacesItemDecoration(20)
        )
        binding.recyclerView.adapter =
            ToastsAdapter(
                this,
                getToasts().toTypedArray(),
                this
            )
    }

    private fun getToasts(): List<ToastItem> {
        val toastsJson = getJsonDataFromAsset(this, "toasts.json")
        return Gson().fromJson(toastsJson, object : TypeToken<List<ToastItem>>() {}.type)
    }

    override fun onItemClicked(toastItem: ToastItem) {
        binding.cartSelectedItemName.text = toastItem.name
        binding.cartSelectedItemPrice.text = toastItem.price
    }

}
