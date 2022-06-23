package com.omidrezabagherian.totishop.core

import android.app.Application
import androidx.hilt.work.HiltWorkerFactory
import androidx.work.Configuration
import dagger.hilt.android.HiltAndroidApp
import javax.inject.Inject

@HiltAndroidApp
class BaseApplication : Application(), Configuration.Provider {
    @Inject
    lateinit var workFactory: HiltWorkerFactory

    override fun getWorkManagerConfiguration() =
        Configuration.Builder().setWorkerFactory(workFactory).build()

}