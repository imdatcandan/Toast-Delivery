package com.imdatcandan.toastdelivery.view

sealed class ViewState {
    data class Success(val message: String) : ViewState()
    data class Error(val message: String) : ViewState()
    data class Loading(val isLoading: Boolean) : ViewState()
}