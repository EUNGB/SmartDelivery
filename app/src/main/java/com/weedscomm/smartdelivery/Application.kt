package com.weedscomm.smartdelivery

import android.app.Application
import com.weedscomm.smartdelivery.di.module.appModules
import net.danlew.android.joda.JodaTimeAndroid
import org.koin.android.ext.koin.androidContext
import org.koin.android.ext.koin.androidLogger
import org.koin.core.context.startKoin
import org.koin.core.logger.Level
import timber.log.Timber

class Application : Application() {

    override fun onCreate() {
        super.onCreate()
        JodaTimeAndroid.init(this)
        Timber.plant(Timber.DebugTree())
        startKoin {
            androidLogger(Level.NONE)
            androidContext(this@Application)
            modules(appModules)
        }
    }
}