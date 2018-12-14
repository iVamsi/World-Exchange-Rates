package com.vamsi.xchangerates.app.utils

import android.content.Context
import android.net.ConnectivityManager
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class NetworkUtils @Inject constructor(var context: Context) {

    val isConnectedToInternet: Boolean?
        get() {
            val conManager = context.getSystemService(Context.CONNECTIVITY_SERVICE)
                    as ConnectivityManager

            val ni = conManager.activeNetworkInfo
            return ni != null && ni.isConnected
        }
}