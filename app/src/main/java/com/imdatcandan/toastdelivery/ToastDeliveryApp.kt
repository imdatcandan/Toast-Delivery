package com.imdatcandan.toastdelivery

import android.app.Application
import com.imdatcandan.toastdelivery.di.appModule
import com.imdatcandan.toastdelivery.di.networkModule
import org.koin.android.ext.koin.androidContext
import org.koin.android.ext.koin.androidLogger
import org.koin.core.context.GlobalContext.startKoin

class ToastDeliveryApp : Application() {

    override fun onCreate() {
        super.onCreate()

        startKoin {
            androidLogger()
            androidContext(this@ToastDeliveryApp)
            modules(arrayListOf(appModule, networkModule))
        }
    }
}