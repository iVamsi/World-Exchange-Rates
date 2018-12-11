package com.vamsi.xchangerates.app.viewmodels

import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.vamsi.xchangerates.app.database.CurrencyDao
import com.vamsi.xchangerates.app.database.CurrencyRepository
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

    private var currencyList = MutableLiveData<List<CurrencyDao.CurrencyUIModel>>()
    private val compositeDisposable = CompositeDisposable()

    init {
        compositeDisposable.add(currencyRepository
            .getUpdatedCurrencies()
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribeWith(object : DisposableObserver<List<CurrencyDao.CurrencyUIModel>>() {

                override fun onError(e: Throwable) {
                    Log.e("AllCurrenciesViewModel", e.stackTrace.toString())
                }

                override fun onNext(data: List<CurrencyDao.CurrencyUIModel>) {
                    currencyList.value = data
                }

                override fun onComplete() {

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