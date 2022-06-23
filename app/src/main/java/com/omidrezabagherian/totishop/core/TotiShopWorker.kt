package com.omidrezabagherian.totishop.core

import android.app.Notification
import android.app.NotificationManager
import android.content.Context
import android.graphics.Bitmap
import android.os.Build
import androidx.annotation.RequiresApi
import androidx.core.content.ContextCompat
import androidx.hilt.work.HiltWorker
import androidx.work.CoroutineWorker
import androidx.work.ForegroundInfo
import androidx.work.WorkerParameters
import com.bumptech.glide.Glide
import com.omidrezabagherian.totishop.core.Values.ID_LAST_PRODUCT_SHARED_PREFERENCES
import com.omidrezabagherian.totishop.core.Values.NOTIFICATION_ID
import com.omidrezabagherian.totishop.core.Values.SHARED_PREFERENCES
import com.omidrezabagherian.totishop.core.Values.mainSharedPreferences
import com.omidrezabagherian.totishop.data.ShopRepository
import dagger.assisted.Assisted
import dagger.assisted.AssistedInject
import java.util.*

@HiltWorker
class TotiShopWorker @AssistedInject constructor(
    @Assisted appContext: Context,
    @Assisted workerParams: WorkerParameters,
    private val repository: ShopRepository
) : CoroutineWorker(appContext, workerParams) {

    override suspend fun getForegroundInfo(): ForegroundInfo {
        return ForegroundInfo(
            NOTIFICATION_ID, createNotification()
        )
    }

    private fun createNotification(): Notification {
        TODO()
    }

    @RequiresApi(Build.VERSION_CODES.O)
    override suspend fun doWork(): Result {

        val productsDateMap = HashMap<String, String>().apply {
            put("orderby", "date")
        }

        val lastProductId = mainSharedPreferences.getInt(ID_LAST_PRODUCT_SHARED_PREFERENCES, 0)

        val data = repository.getProductList(1, 1, productsDateMap)
        data.collect {
            if (it is ResultWrapper.Success) {
                val productName = it.value.first().name
                //if (lastProductId != it.value.first().id) {
                    val productImage = getImage(it.value.first().images.first().src)
                    val notificationManager = ContextCompat.getSystemService(
                        applicationContext,
                        NotificationManager::class.java,
                    ) as NotificationManager
                    notificationManager.sendNotification(
                        productName,
                        productImage,
                        applicationContext
                    )

                    val sharedPreferencesEditor = mainSharedPreferences.edit()
                    sharedPreferencesEditor.putInt(
                        ID_LAST_PRODUCT_SHARED_PREFERENCES,
                        it.value.first().id
                    )
                    sharedPreferencesEditor.apply()
                    sharedPreferencesEditor.commit()

                //}
            }
        }

        return Result.success()
    }

    private fun getImage(url: String): Bitmap? {
        val futureTarget = Glide.with(applicationContext)
            .asBitmap()
            .load(url)
            .submit(100, 100)

        return futureTarget.get()
    }

}