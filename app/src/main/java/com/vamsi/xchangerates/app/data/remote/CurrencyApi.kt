package com.vamsi.xchangerates.app.data.remote

import com.vamsi.xchangerates.app.model.CurrencyResponse
import com.vamsi.xchangerates.app.utils.Constants.ACCESS_KEY
import com.vamsi.xchangerates.app.utils.Constants.FORMAT
import com.vamsi.xchangerates.app.utils.Constants.LATEST
import retrofit2.http.GET
import retrofit2.http.Query

/**
 * Defines the abstract methods used for interacting with the Currency API.
 */
interface CurrencyApi {

    @GET(LATEST)
    suspend fun fetchCurrencies(
        @Query(ACCESS_KEY) accessKey: String,
        @Query(FORMAT) format: String
    ): CurrencyResponse
}