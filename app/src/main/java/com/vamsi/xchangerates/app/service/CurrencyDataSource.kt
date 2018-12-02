package com.vamsi.xchangerates.app.service

import com.vamsi.xchangerates.app.utils.ACCESS_KEY_API_LAYER
import com.vamsi.xchangerates.app.utils.FORMAT_TYPE
import javax.inject.Inject

class CurrencyDataSource @Inject constructor(private val currencyService: CurrencyService) {

    fun requestUpdatedCurrencies(currencies: String) =
        currencyService.requestUpdatedCurrencies(
            ACCESS_KEY_API_LAYER, currencies, FORMAT_TYPE)
}