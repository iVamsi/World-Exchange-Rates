package com.vamsi.xchangerates.app.data.remote

import com.vamsi.xchangerates.app.model.CurrencyResponse
import com.vamsi.xchangerates.app.utils.ACCESS_KEY
import com.vamsi.xchangerates.app.utils.FORMAT
import com.vamsi.xchangerates.app.utils.LATEST
import io.reactivex.Observable
import retrofit2.http.GET
import retrofit2.http.Query

interface CurrencyService {

    @GET(LATEST)
    fun requestUpdatedCurrencies(
        @Query(ACCESS_KEY) accessKey: String,
        @Query(FORMAT) format: String
    ): Observable<CurrencyResponse>
}