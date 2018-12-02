package com.vamsi.xchangerates.app.model

import com.google.gson.annotations.SerializedName
import com.vamsi.xchangerates.app.utils.QUOTES
import com.vamsi.xchangerates.app.utils.SUCCESS

data class CurrencyResponse(
    @SerializedName(SUCCESS) val isSuccess: Boolean,
    @SerializedName(QUOTES) val currencyQuotes: Map<String, Double>
)