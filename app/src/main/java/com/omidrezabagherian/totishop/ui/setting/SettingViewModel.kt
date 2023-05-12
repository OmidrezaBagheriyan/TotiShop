package com.omidrezabagherian.totishop.ui.setting

import android.os.Build.ID
import androidx.lifecycle.ViewModel
import androidx.work.*
import com.omidrezabagherian.totishop.util.TotiShopWorker
import com.omidrezabagherian.totishop.util.Values.DATA_NAME
import dagger.hilt.android.lifecycle.HiltViewModel
import java.util.concurrent.TimeUnit
import javax.inject.Inject

@HiltViewModel
class SettingViewModel @Inject constructor() : ViewModel() {

    lateinit var workManager: WorkManager
    private lateinit var periodicWork: PeriodicWorkRequest
    var time = "3"
    val data = Data.Builder().putString(DATA_NAME, time)

    fun setTimeWorkManager() {

        periodicWork = PeriodicWorkRequestBuilder<TotiShopWorker>(time.toLong(), TimeUnit.HOURS)
            .setInputData(data.build())
            .build()
    }

    fun startWorkManager() {
        setWorkManager()
    }

    fun stopWorkManager() {
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