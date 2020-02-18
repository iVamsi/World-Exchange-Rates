package com.vamsi.xchangerates.app.core.extension

import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.LiveData
import androidx.lifecycle.Observer
import com.vamsi.xchangerates.app.core.exception.Failure

inline fun <T> LifecycleOwner.observe(liveData: LiveData<T>, crossinline body: (T) -> Unit = {}) {
        liveData.observe(this, Observer { it?.let { t -> body(t) } })
}

inline fun LifecycleOwner.failure(liveData: LiveData<Failure>, crossinline body: (Failure) -> Unit = {}) {
        liveData.observe(this, Observer { it?.let { t -> body(t) } })
}