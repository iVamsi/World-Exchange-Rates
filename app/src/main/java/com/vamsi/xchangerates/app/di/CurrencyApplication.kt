package com.vamsi.xchangerates.app.di

import dagger.android.AndroidInjector
import dagger.android.support.DaggerApplication

class CurrencyApplication : DaggerApplication() {
    override fun applicationInjector(): AndroidInjector<out DaggerApplication> {
        return DaggerAppComponent.builder().create(this)
    }
}