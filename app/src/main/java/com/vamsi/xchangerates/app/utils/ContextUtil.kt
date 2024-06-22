package com.vamsi.xchangerates.app.utils

import android.content.Context
import dagger.hilt.android.qualifiers.ApplicationContext
import javax.inject.Inject

/**
 * Utility class to provide context to classes that require it
 */
class ContextUtil @Inject constructor(
    @ApplicationContext private val context: Context
) {
    fun getContext(): Context {
        return context
    }
}
