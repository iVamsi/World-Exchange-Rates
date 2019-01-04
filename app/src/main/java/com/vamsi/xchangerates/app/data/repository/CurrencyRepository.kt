package com.vamsi.xchangerates.app.data.repository

import com.vamsi.xchangerates.app.data.local.AppDatabase
import com.vamsi.xchangerates.app.data.local.CurrencyResponseEntity
import com.vamsi.xchangerates.app.data.remote.CurrencyDataSource
import com.vamsi.xchangerates.app.model.CurrencyResponse
import com.vamsi.xchangerates.app.model.CurrencyUIModel
import com.vamsi.xchangerates.app.utils.NetworkUtils
import io.reactivex.Observable
import io.reactivex.disposables.Disposable
import javax.inject.Inject
import javax.inject.Singleton

/**
 * Repository module for handling data operations.
 */
@Singleton
class CurrencyRepository @Inject constructor(
    private val appDatabase: AppDatabase,
    private val currencyDataSource: CurrencyDataSource
) {

    @Inject lateinit var networkUtils: NetworkUtils

    val allCompositeDisposable: MutableList<Disposable> = arrayListOf()

    fun getCurrencies() = appDatabase.currencyDao().getCurrencies()

    fun getCurrency(currencyId: String) = appDatabase.currencyDao().getCurrency(currencyId)

    fun getCurrencyFavorites(): Observable<List<CurrencyUIModel>> {
        return getCurrencyFavoritesFromDb()
    }

    fun getUpdatedCurrencies(): Observable<List<CurrencyUIModel>> {
        return getCurrenciesFromNetwork()
    }

    fun getCurrenciesFromNetwork(): Observable<List<CurrencyUIModel>> {
        networkUtils.isConnectedToInternet?.let {
            if (it) {
                return currencyDataSource.requestUpdatedCurrencies().flatMap {
                    return@flatMap saveCurrencyResponse(it)
                }
            }
        }
        return getCurrenciesFromDatabase()
    }

    fun saveCurrencyResponse(currencyResponse: CurrencyResponse): Observable<List<CurrencyUIModel>> {
        insertCurrencyResponse(currencyResponse)
        return getCurrenciesFromDatabase()
    }

    fun getCurrenciesFromDatabase(): Observable<List<CurrencyUIModel>> {
        return appDatabase.currencyDao().getCurrenciesForUI().toObservable()
    }

    fun getCurrencyFavoritesFromDb(): Observable<List<CurrencyUIModel>> {
        return appDatabase.currencyDao().getCurrencyFavoritesForUI().toObservable()
    }

    fun insertCurrencyResponse(currencyResponse: CurrencyResponse) {
            val currencyList = ArrayList<CurrencyResponseEntity>()
            currencyResponse.currencyQuotes.forEach { (currId, currValue) ->
                currencyList.add(
                    CurrencyResponseEntity(
                        currId.substring(
                            3
                        ), currValue
                    )
                )
            }
            appDatabase.currencyDao().updateCurrencies(currencyList)
    }
}