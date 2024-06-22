package com.vamsi.xchangerates.app.model

import com.vamsi.xchangerates.app.utils.Constants.RATES
import com.vamsi.xchangerates.app.utils.Constants.SUCCESS
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class CurrencyResponse(
    @SerialName(value = SUCCESS) val isSuccess: Boolean,
    @SerialName(value = RATES) val currencyQuotes: Map<String, Double>
)
