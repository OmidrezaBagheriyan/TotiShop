package com.omidrezabagherian.totishop.util

import android.app.NotificationManager
import android.content.Context
import android.graphics.Bitmap
import android.os.Build
import android.util.Log
import androidx.annotation.RequiresApi
import androidx.core.content.ContextCompat
import androidx.hilt.work.HiltWorker
import androidx.work.CoroutineWorker
import androidx.work.WorkerParameters
import com.bumptech.glide.Glide
import com.omidrezabagherian.totishop.util.Values.ID_LAST_PRODUCT_SHARED_PREFERENCES
import com.omidrezabagherian.totishop.util.Values.SHARED_PREFERENCES
import com.omidrezabagherian.totishop.util.Values.mainSharedPreferences
import com.omidrezabagherian.totishop.domain.repositories.ShopRepository
import dagger.assisted.Assisted
import dagger.assisted.AssistedInject
import java.util.*

@HiltWorker
class TotiShopWorker @AssistedInject constructor(
    @Assisted appContext: Context,
    @Assisted workerParams: WorkerParameters,
    private val repository: ShopRepository
) : CoroutineWorker(appContext, workerParams) {

    @RequiresApi(Build.VERSION_CODES.O)
    override suspend fun doWork(): Result {

        val productsDateMap = HashMap<String, String>().apply {
            put("orderby", "date")
        }

        mainSharedPreferences =
            applicationContext.getSharedPreferences(SHARED_PREFERENCES, Context.MODE_PRIVATE)
        val lastProductId = mainSharedPreferences.getInt(ID_LAST_PRODUCT_SHARED_PREFERENCES, 0)

        val data = repository.getProductList(1, 1, productsDateMap)
        data.collect {
            if (it is ResultWrapper.Success) {
                val productId = it.value.first().id
                val productName = it.value.first().name

                //if (lastProductId != it.value.first().id) {
                    val productImage = getImage(it.value.first().images.first().src)
                    val notificationManager = ContextCompat.getSystemService(
                        applicationContext,
                        NotificationManager::class.java,
                    ) as NotificationManager
                    notificationManager.sendNotification(
                        productId,
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
                    Log.i("Test-MAN",it.value.toString())

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