package com.vamsi.xchangerates.app.data.remote

import com.vamsi.xchangerates.app.BuildConfig
import com.vamsi.xchangerates.app.utils.FORMAT_TYPE
import javax.inject.Inject

class CurrencyDataSource @Inject constructor(private val currencyService: CurrencyService) {

    fun requestUpdatedCurrencies() =
        currencyService.requestUpdatedCurrencies(
            BuildConfig.CURRENCY_API_KEY, FORMAT_TYPE)
}