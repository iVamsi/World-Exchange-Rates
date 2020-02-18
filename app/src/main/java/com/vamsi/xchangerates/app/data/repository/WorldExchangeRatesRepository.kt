package com.vamsi.xchangerates.app.data.repository

import com.vamsi.xchangerates.app.core.exception.Failure
import com.vamsi.xchangerates.app.core.functional.Either
import com.vamsi.xchangerates.app.data.local.CurrencyResponseEntity
import com.vamsi.xchangerates.app.model.CurrencyUIModel

interface WorldExchangeRatesRepository {

    suspend fun initializeDatabase()

    suspend fun getCountOfCurrenciesInDatabase(): Int

    suspend fun fetchCurrencies(): Either<Failure, List<CurrencyUIModel>>

    suspend fun insertCurrencyResponse(currencyList: List<CurrencyResponseEntity>)

    suspend fun updateCurrencyFavorite(currencyId: String, favorite: String = "yes")

    suspend fun getFavoriteCurrencies(): List<CurrencyUIModel>

    suspend fun getCurrenciesFromDatabase(): List<CurrencyUIModel>
}