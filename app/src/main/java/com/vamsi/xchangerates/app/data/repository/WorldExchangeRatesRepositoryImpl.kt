package com.vamsi.xchangerates.app.data.repository

import com.vamsi.xchangerates.app.BuildConfig
import com.vamsi.xchangerates.app.data.local.AppDatabase
import com.vamsi.xchangerates.app.data.local.CurrencyResponseEntity
import com.vamsi.xchangerates.app.data.remote.WorldExchangeRatesService
import com.vamsi.xchangerates.app.model.CurrencyResponse
import com.vamsi.xchangerates.app.model.CurrencyUIModel
import com.vamsi.xchangerates.app.utils.FORMAT_TYPE
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import javax.inject.Inject

class WorldExchangeRatesRepositoryImpl @Inject constructor(
    private val worldExchangeRatesService: WorldExchangeRatesService,
    private val appDatabase: AppDatabase
) : WorldExchangeRatesRepository {

    override suspend fun getFavoriteCurrencies(): List<CurrencyUIModel> {
        return withContext(Dispatchers.Default) {
            appDatabase.currencyDao().getCurrencyFavoritesForUI()
        }
    }

    override suspend fun updateCurrencyFavorite(currencyId: String, favorite: String) {
        withContext(Dispatchers.IO) {
            appDatabase.currencyDao().updateCurrencyFavorite(currencyId, favorite)
        }
    }

    override suspend fun fetchCurrencies(): List<CurrencyUIModel> {
        val currencyResponse =
            worldExchangeRatesService.fetchCurrencies(BuildConfig.CURRENCY_API_KEY, FORMAT_TYPE)
        insertCurrencyResponse(currencyResponse)
        return getCurrenciesFromDatabase()
    }

    override suspend fun insertCurrencyResponse(currencyResponse: CurrencyResponse) {
        withContext(Dispatchers.Default) {
            appDatabase.currencyDao()
                .updateCurrencies(getCurrencyListFromResponse(currencyResponse))
        }
    }

    private fun getCurrencyListFromResponse(currencyResponse: CurrencyResponse):
        List<CurrencyResponseEntity> {
        val currencyList = ArrayList<CurrencyResponseEntity>()
        currencyResponse.currencyQuotes.forEach { (currId, currValue) ->
            currencyList.add(
                CurrencyResponseEntity(
                    currId, currValue
                )
            )
        }
        return currencyList
    }

    override suspend fun getCurrenciesFromDatabase(): List<CurrencyUIModel> {
        return withContext(Dispatchers.Default) {
            appDatabase.currencyDao().getCurrenciesForUI()
        }
    }
}