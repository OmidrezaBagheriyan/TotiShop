package com.omidrezabagherian.totishop.ui.setting

import android.content.SharedPreferences
import android.os.Build.ID
import androidx.lifecycle.ViewModel
import androidx.work.*
import com.omidrezabagherian.totishop.core.TotiShopWorker
import com.omidrezabagherian.totishop.core.Values.DATA_NAME
import com.omidrezabagherian.totishop.data.ShopRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import java.util.concurrent.TimeUnit
import javax.inject.Inject

@HiltViewModel
class SettingViewModel @Inject constructor(private val shopRepository: ShopRepository) :
    ViewModel() {

    var time = "3"
    lateinit var workManager: WorkManager
    private lateinit var periodicWork: PeriodicWorkRequest
    val data = Data.Builder().putString(DATA_NAME, time)

    fun setTime() {
        periodicWork = PeriodicWorkRequestBuilder<TotiShopWorker>(time.toLong(), TimeUnit.HOURS)
            .setInputData(data.build())
            .build()

        setWorkManager()
    }

    fun start() {
        periodicWork = PeriodicWorkRequestBuilder<TotiShopWorker>(3, TimeUnit.HOURS)
            .setInputData(data.build())
            .build()

        setWorkManager()
    }

    fun stop() {
        workManager.cancelUniqueWork(ID)
    }

    private fun setWorkManager() {
        workManager.enqueueUniquePeriodicWork(
            ID,
            ExistingPeriodicWorkPolicy.REPLACE,
            periodicWork
        )
    }
}