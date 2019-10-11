package com.vamsi.xchangerates.app.data.repository

import com.vamsi.xchangerates.app.model.CurrencyResponse
import com.vamsi.xchangerates.app.model.CurrencyUIModel

interface WorldExchangeRatesRepository {
    suspend fun fetchCurrencies(): List<CurrencyUIModel>

    suspend fun insertCurrencyResponse(currencyResponse: CurrencyResponse)

    suspend fun updateCurrencyFavorite(currencyId: String, favorite: String = "yes")

    suspend fun getFavoriteCurrencies(): List<CurrencyUIModel>

    suspend fun getCurrenciesFromDatabase(): List<CurrencyUIModel>
}