package com.imdatcandan.toastdelivery

import androidx.annotation.CallSuper
import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import org.junit.Before
import org.junit.Rule

abstract class BaseUnitTest<T : Any> {

    @get:Rule
    val instantTaskExecutorRule = InstantTaskExecutorRule()

    protected lateinit var tested: T

    @Before
    @CallSuper
    open fun setUp() {
        tested = initSelf()
    }

    protected abstract fun initSelf(): T

}