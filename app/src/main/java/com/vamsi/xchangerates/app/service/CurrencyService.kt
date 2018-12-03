package com.vamsi.xchangerates.app.service

import com.vamsi.xchangerates.app.model.CurrencyResponse
import com.vamsi.xchangerates.app.utils.ACCESS_KEY
import com.vamsi.xchangerates.app.utils.CURRENCIES
import com.vamsi.xchangerates.app.utils.FORMAT
import com.vamsi.xchangerates.app.utils.LIVE
import io.reactivex.Observable
import retrofit2.http.GET
import retrofit2.http.Query

interface CurrencyService {

    @GET(LIVE)
    fun requestUpdatedCurrencies(
        @Query(ACCESS_KEY) accessKey: String,
        @Query(FORMAT) format: String
    ): Observable<CurrencyResponse>

    @GET(LIVE)
    fun getCurrencyValue(
        @Query(ACCESS_KEY) accessKey: String,
        @Query(CURRENCIES) currencies: String,
        @Query(FORMAT) format: String
    ): Observable<CurrencyResponse>

}