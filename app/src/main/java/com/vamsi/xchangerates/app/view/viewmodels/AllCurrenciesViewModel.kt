package com.vamsi.xchangerates.app.view.viewmodels

import androidx.databinding.ObservableField
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.vamsi.xchangerates.app.data.repository.CurrencyRepository
import com.vamsi.xchangerates.app.model.CurrencyUIModel
import io.reactivex.Completable
import io.reactivex.CompletableObserver
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.annotations.NonNull
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.disposables.Disposable
import io.reactivex.observers.DisposableObserver
import io.reactivex.schedulers.Schedulers
import timber.log.Timber
import javax.inject.Inject

/**
 * The ViewModel for [AllCurrenciesFragment].
 */
class AllCurrenciesViewModel @Inject constructor(
    private val currencyRepository: CurrencyRepository
) : ViewModel() {

    private var currencyList = MutableLiveData<List<CurrencyUIModel>>()
    private val compositeDisposable = CompositeDisposable()

    var isLoading = ObservableField(false)

    init {
        isLoading.set(true)
        initLocalCurrencies()
    }

    private fun initLocalCurrencies() {
        val disposable = currencyRepository.getTotalCurrencies()
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe {
                if (isDatabaseEmpty(it)) {
                    initializeDatabase()
                } else {
                    fetchUpdatedCurrencies()
                    Timber.tag("SplashViewModel").i("Database has already been initialized")
                }
            }
        compositeDisposable.add(disposable)
    }

    private fun initializeDatabase() {
        Completable.fromAction {
            currencyRepository.initCurrenciesInDatabase()
        }
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe(object : CompletableObserver {
                override fun onSubscribe(@NonNull d: Disposable) {
                    compositeDisposable.add(d)
                }

                override fun onComplete() {
                    Timber.tag("SplashViewModel").i("Database has been initialized")
                    fetchUpdatedCurrencies()
                }

                override fun onError(@NonNull e: Throwable) {
                    e.printStackTrace()
                    Timber.tag("SplashViewModel").e("Unable to initialize database")
                }
            })
    }

    private fun isDatabaseEmpty(currenciesTotal: Int) = currenciesTotal == 0

    private fun fetchUpdatedCurrencies() {
        compositeDisposable.add(currencyRepository
            .getUpdatedCurrencies()
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribeWith(object : DisposableObserver<List<CurrencyUIModel>>() {

                override fun onError(t: Throwable) {
                    Timber.tag("AllCurrenciesViewModel").e(t.stackTrace.toString())
                    isLoading.set(false)
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

    fun updateCurrencyFavorite(currencyId: String) {
        currencyRepository.updateCurrencyFavorite(currencyId, "yes")
    }

    override fun onCleared() {
        super.onCleared()
        compositeDisposable.clear()
    }
}