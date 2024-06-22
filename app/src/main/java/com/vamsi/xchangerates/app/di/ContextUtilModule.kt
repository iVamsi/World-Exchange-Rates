package com.vamsi.xchangerates.app.di

import android.content.Context
import com.vamsi.xchangerates.app.utils.ContextUtil
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent

/**
 * DI module for [ContextUtil]
 */
@Module
@InstallIn(SingletonComponent::class)
object ContextUtilModule {

    @Provides
    fun provideContextUtil(@ApplicationContext context: Context): ContextUtil {
        return ContextUtil(context)
    }

}
