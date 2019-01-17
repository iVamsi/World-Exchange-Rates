package com.vamsi.xchangerates.app.data.repository

import com.vamsi.xchangerates.app.data.local.AppDatabase
import com.vamsi.xchangerates.app.data.local.CurrencyResponseEntity
import com.vamsi.xchangerates.app.data.remote.CurrencyDataSource
import com.vamsi.xchangerates.app.model.CurrencyResponse
import com.vamsi.xchangerates.app.model.CurrencyUIModel
import com.vamsi.xchangerates.app.utils.NetworkUtils
import io.reactivex.Observable
import io.reactivex.schedulers.Schedulers
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

    @Inject
    lateinit var networkUtils: NetworkUtils

    fun getCurrencyFavorites(): Observable<List<CurrencyUIModel>> {
        return getCurrencyFavoritesFromDb()
    }

    fun getUpdatedCurrencies(): Observable<List<CurrencyUIModel>> {
        return getCurrenciesFromNetwork()
    }

    private fun getCurrenciesFromNetwork(): Observable<List<CurrencyUIModel>> {
        networkUtils.isConnectedToInternet?.let {
            if (it) {
                return currencyDataSource.requestUpdatedCurrencies().flatMap {
                    return@flatMap saveCurrencyResponse(it)
                }
            }
        }
        return getCurrenciesFromDatabase()
    }

    private fun saveCurrencyResponse(currencyResponse: CurrencyResponse): Observable<List<CurrencyUIModel>> {
        insertCurrencyResponse(currencyResponse)
        return getCurrenciesFromDatabase()
    }

    fun getCurrenciesFromDatabase(): Observable<List<CurrencyUIModel>> {
        return appDatabase.currencyDao().getCurrenciesForUI().toObservable()
    }

    private fun getCurrencyFavoritesFromDb(): Observable<List<CurrencyUIModel>> {
        return appDatabase.currencyDao().getCurrencyFavoritesForUI().toObservable()
    }

    fun updateCurrencyFavorite(currencyId: String, favorite: String) {
        Schedulers.io().scheduleDirect {
            appDatabase.currencyDao().updateCurrencyFavorite(currencyId, favorite)
        }
    }

    private fun insertCurrencyResponse(currencyResponse: CurrencyResponse) {
        Schedulers.io().scheduleDirect {
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
}