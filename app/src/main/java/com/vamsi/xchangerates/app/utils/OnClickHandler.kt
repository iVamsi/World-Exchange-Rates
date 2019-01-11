package com.vamsi.xchangerates.app.utils

import android.view.View
import com.vamsi.xchangerates.app.model.CurrencyUIModel

interface OnClickHandler {
    fun onClick(view: View)
    fun onItemClicked(currencyId: CurrencyUIModel)
}