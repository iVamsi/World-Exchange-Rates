package com.vamsi.xchangerates.app.utils

import android.view.View

interface OnClickHandler {
    fun onClick(view: View)
    fun onItemLongClick(currencyId: String): Boolean
    fun onItemClicked(currencyId: String)
}