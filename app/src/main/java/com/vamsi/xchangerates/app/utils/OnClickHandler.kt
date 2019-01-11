package com.vamsi.xchangerates.app.utils

import android.view.View

interface OnClickHandler {
    fun onClick(view: View)
    fun onItemClicked(currencyId: String)
}