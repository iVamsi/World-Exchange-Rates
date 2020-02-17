package com.vamsi.xchangerates.app.core.extension

import android.content.Context
import android.icu.lang.UCharacter.GraphemeClusterBreak.T
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentTransaction
import androidx.lifecycle.*
import androidx.lifecycle.ViewModelProvider.Factory
import com.vamsi.xchangerates.app.utils.AutoClearedValue
import kotlin.properties.ObservableProperty
import kotlin.properties.ReadWriteProperty
import kotlin.reflect.KProperty

inline fun FragmentManager.transaction(func: FragmentTransaction.() -> FragmentTransaction) =
        beginTransaction().func().commit()

inline fun <reified VM : ViewModel> Fragment.viewModel(factory: Factory, body: VM.() -> Unit): VM
        = ViewModelProviders.of(this, factory)[VM::class.java].apply(body)

inline fun <reified VM : ViewModel> Fragment.viewModelProvider(
    provider: Factory
) = ViewModelProviders.of(this, provider).get(VM::class.java)

inline fun <T> observer(
    initialValue: T, crossinline onChange: (newValue: T) -> Unit
):
        ReadWriteProperty<Any?, T> = object : ObservableProperty<T>(initialValue) {
    override fun afterChange(property: KProperty<*>, oldValue: T, newValue: T) = onChange(newValue)
}

val Fragment.appContext: Context get() = activity?.applicationContext!!

fun <T : Any> Fragment.autoCleared() = AutoClearedValue<T>(this)