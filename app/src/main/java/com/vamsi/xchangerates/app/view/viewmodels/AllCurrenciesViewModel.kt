package com.vamsi.xchangerates.app.view.viewmodels

import androidx.databinding.ObservableField
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.vamsi.xchangerates.app.data.repository.WorldExchangeRatesRepository
import com.vamsi.xchangerates.app.model.CurrencyUIModel
import kotlinx.coroutines.launch
import javax.inject.Inject

class AllCurrenciesViewModel @Inject constructor(
    private val worldExchangeRatesRepository: WorldExchangeRatesRepository
) : ViewModel() {

    private var currencyList = MutableLiveData<List<CurrencyUIModel>>()

    var isLoading = ObservableField(false)

    init {
        isLoading.set(true)
        fetchCurrencies()
    }

    // private fun initLocalCurrencies() {
    //     val disposable = currencyRepository.getTotalCurrencies()
    //         .subscribeOn(Schedulers.io())
    //         .observeOn(AndroidSchedulers.mainThread())
    //         .subscribe {
    //             if (isDatabaseEmpty(it)) {
    //                 initializeDatabase()
    //             } else {
    //                 fetchUpdatedCurrencies()
    //                 Log.i("SplashViewModel", "Database has already been initialized")
    //             }
    //         }
    //     compositeDisposable.add(disposable)
    // }
    //
    // private fun initializeDatabase() {
    //     Completable.fromAction {
    //         currencyRepository.initCurrenciesInDatabase()
    //     }
    //         .subscribeOn(Schedulers.io())
    //         .observeOn(AndroidSchedulers.mainThread())
    //         .subscribe(object : CompletableObserver {
    //             override fun onSubscribe(@NonNull d: Disposable) {
    //                 compositeDisposable.add(d)
    //             }
    //
    //             override fun onComplete() {
    //                 Log.i("SplashViewModel", "Database has been initialized")
    //                 fetchUpdatedCurrencies()
    //             }
    //
    //             override fun onError(@NonNull e: Throwable) {
    //                 e.printStackTrace()
    //                 Log.e("SplashViewModel", "Unable to initialize database")
    //             }
    //         })
    // }

    private fun isDatabaseEmpty(currenciesTotal: Int) = currenciesTotal == 0


    private fun fetchCurrencies() {
        viewModelScope.launch {
            currencyList.value = worldExchangeRatesRepository.fetchCurrencies()
            isLoading.set(false)
        }
    }

    fun getCurrencyList() = currencyList

    fun updateCurrencyFavorite(currencyId: String) {
        viewModelScope.launch {
            worldExchangeRatesRepository.updateCurrencyFavorite(currencyId)
        }
    }
}