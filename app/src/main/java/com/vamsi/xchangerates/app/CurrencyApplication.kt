package com.vamsi.xchangerates.app

import android.app.Activity
import android.app.Application
import androidx.fragment.app.Fragment
import com.vamsi.xchangerates.app.di.ContextModule
import com.vamsi.xchangerates.app.di.DaggerAppComponent
import com.vamsi.xchangerates.app.di.DatabaseModule
import com.vamsi.xchangerates.app.di.ServiceModule
import dagger.android.DispatchingAndroidInjector
import dagger.android.HasActivityInjector
import dagger.android.support.HasSupportFragmentInjector
import javax.inject.Inject

class CurrencyApplication : Application(), HasActivityInjector
    , HasSupportFragmentInjector {

    @Inject lateinit var activityInjector: DispatchingAndroidInjector<Activity>
    @Inject lateinit var supportFragmentInjector: DispatchingAndroidInjector<Fragment>

    override fun onCreate() {
        super.onCreate()
        initDagger()
    }

    fun initDagger() {
        DaggerAppComponent.builder()
            .contextModule(ContextModule(this))
            .databaseModule(DatabaseModule())
            .serviceModule(ServiceModule())
            .build()
            .inject(this)
    }

    override fun activityInjector() = activityInjector

    override fun supportFragmentInjector() = supportFragmentInjector
}