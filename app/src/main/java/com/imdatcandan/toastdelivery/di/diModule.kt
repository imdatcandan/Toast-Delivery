package com.imdatcandan.toastdelivery.di

import android.app.Application
import android.content.Context
import android.content.SharedPreferences
import com.google.gson.Gson
import com.google.gson.GsonBuilder
import com.imdatcandan.toastdelivery.BuildConfig
import com.imdatcandan.toastdelivery.checkout.CheckoutApi
import com.imdatcandan.toastdelivery.checkout.CheckoutRepository
import com.imdatcandan.toastdelivery.checkout.CheckoutUseCase
import com.imdatcandan.toastdelivery.login.LoginApi
import com.imdatcandan.toastdelivery.login.LoginRepository
import com.imdatcandan.toastdelivery.login.LoginUseCase
import com.imdatcandan.toastdelivery.login.LoginUseCase.Companion.ACCESS_TOKEN
import com.imdatcandan.toastdelivery.login.TokenInterceptor
import com.imdatcandan.toastdelivery.viewmodel.MainViewModel
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import org.koin.android.ext.koin.androidApplication
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.core.module.Module
import org.koin.dsl.module
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

val appModule = module {
    single { LoginRepository(get()) }
    single { LoginUseCase(get(), get(), get()) }
    single { CheckoutRepository(get()) }
    single { CheckoutUseCase(get(), get()) }
    viewModel { MainViewModel(get(), get()) }
}

val networkModule: Module = module {
    single { provideLoggingInterceptor() }
    single { provideTokenInterceptor(get()) }
    single { provideOkHttpClient(get(), get()) }
    single { provideRetrofit(get()) }
    single { provideCheckoutApi(get()) }
    single { provideLoginApi(get()) }
    single { provideSharedPreferences(androidApplication()) }
    single { provideGson() }

}

private fun provideLoggingInterceptor(): HttpLoggingInterceptor {
    return HttpLoggingInterceptor().apply {
        level = HttpLoggingInterceptor.Level.BODY
    }
}

private fun provideTokenInterceptor(preferences: SharedPreferences): TokenInterceptor {
    return TokenInterceptor(preferences)
}

private fun provideSharedPreferences(application: Application): SharedPreferences {
    return application.getSharedPreferences(
        ACCESS_TOKEN,
        Context.MODE_PRIVATE
    )
}

private fun provideOkHttpClient(
    loggingInterceptor: HttpLoggingInterceptor,
    tokenInterceptor: TokenInterceptor
): OkHttpClient {
    return OkHttpClient.Builder()
        .addInterceptor(loggingInterceptor)
        .addInterceptor(tokenInterceptor)
        .build()
}

private fun provideCheckoutApi(retrofit: Retrofit): CheckoutApi =
    retrofit.create(CheckoutApi::class.java)

private fun provideLoginApi(retrofit: Retrofit): LoginApi =
    retrofit.create(LoginApi::class.java)

private fun provideGson(): Gson = GsonBuilder().create()

private fun provideRetrofit(okHttpClient: OkHttpClient): Retrofit {
    return Retrofit.Builder()
        .baseUrl(BuildConfig.BASE_URL)
        .client(okHttpClient)
        .addConverterFactory(GsonConverterFactory.create())
        .build()
}