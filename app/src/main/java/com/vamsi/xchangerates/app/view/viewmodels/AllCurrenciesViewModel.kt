package com.vamsi.xchangerates.app.view.viewmodels

import android.util.Log
import androidx.databinding.ObservableField
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.vamsi.xchangerates.app.data.repository.CurrencyRepository
import com.vamsi.xchangerates.app.model.CurrencyUIModel
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.observers.DisposableObserver
import io.reactivex.schedulers.Schedulers
import javax.inject.Inject

/**
 * The ViewModel for [AllCurrencies].
 */
class AllCurrenciesViewModel @Inject constructor(
    currencyRepository: CurrencyRepository
) : ViewModel() {

    private var currencyList = MutableLiveData<List<CurrencyUIModel>>()
    private val compositeDisposable = CompositeDisposable()

    var isLoading = ObservableField(false)

    init {
        isLoading.set(true)
        compositeDisposable.add(currencyRepository
            .getUpdatedCurrencies()
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribeWith(object : DisposableObserver<List<CurrencyUIModel>>() {

                override fun onError(e: Throwable) {
                    Log.e("AllCurrenciesViewModel", e.stackTrace.toString())
                }

                override fun onNext(data: List<CurrencyUIModel>) {
                    isLoading.set(false)
                    currencyList.value = data
                }

                override fun onComplete() {
                    isLoading.set(false)
                }
            })
        )
    }

    fun getCurrencyList() = currencyList

    override fun onCleared() {
        super.onCleared()
        compositeDisposable.clear()
    }
}