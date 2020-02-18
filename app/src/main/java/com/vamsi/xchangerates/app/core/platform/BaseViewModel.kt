package com.vamsi.xchangerates.app.core.platform

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.vamsi.xchangerates.app.core.exception.Failure

abstract class BaseViewModel: ViewModel() {

    var failure: MutableLiveData<Failure> = MutableLiveData()

    protected fun handleFailure(failure: Failure) {
        this.failure.value = failure
    }
}