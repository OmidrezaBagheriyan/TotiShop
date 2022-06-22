package com.omidrezabagherian.totishop.core

import android.app.Notification
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.SharedPreferences
import android.graphics.BitmapFactory
import android.os.Build
import android.provider.MediaStore.Images.ImageColumns.DESCRIPTION
import android.widget.RemoteViews
import androidx.annotation.RequiresApi
import androidx.navigation.NavDeepLinkBuilder
import androidx.work.CoroutineWorker
import androidx.work.WorkerParameters
import com.omidrezabagherian.totishop.R
import com.omidrezabagherian.totishop.core.Values.CHANNEL_ID
import com.omidrezabagherian.totishop.core.Values.NOTIFICATION_ID
import com.omidrezabagherian.totishop.domain.model.product.Product
import com.omidrezabagherian.totishop.ui.details.DetailFragmentArgs
import java.text.SimpleDateFormat
import java.util.*

@RequiresApi(Build.VERSION_CODES.O)
class TotiShopWorker(
    private val appContext: Context,
    private val params: WorkerParameters
) : CoroutineWorker(
    appContext,
    params
) {

    private lateinit var totiShopNotificationManager: NotificationManager
    private lateinit var totiShopNotificationChannel: NotificationChannel
    private lateinit var totiShopBuilder: Notification.Builder
    private lateinit var totiShopPendingIntent: PendingIntent
    private lateinit var totiShopCollapseView: RemoteViews

    override suspend fun doWork(): Result {
        val workSharedPreferences: SharedPreferences =
            appContext.getSharedPreferences(Values.SHARED_PREFERENCES, Context.MODE_PRIVATE)
        val inputData = workSharedPreferences.getString(Values.ID_LAST_PRODUCT_SHARED_PREFERENCES,"3")
        val date = lastCheckTime(inputData!!)

        val result = TotiShopNetwork.shopService.getNewProductList(date)
        return if (result.isSuccessful) {
            result.body()?.let {
                collapseView(it[0])
                navDeepLink(it[0].id)
                notificationManagerInit()
                notification()
            }
            Result.success()

        } else Result.retry()
    }

    private fun collapseView(product: Product) {

        totiShopCollapseView = RemoteViews(appContext.packageName, R.layout.notification_layout)
        totiShopCollapseView.setTextViewText(R.id.textViewNotificationTitle, product.name)
        totiShopCollapseView.setTextViewText(R.id.textViewNotificationDate, product.date_created)
        if (product.sale_price.isNotBlank())
            totiShopCollapseView.setTextViewText(R.id.textViewNotificationPrice, product.sale_price)
        else
            totiShopCollapseView.setTextViewText(R.id.textViewNotificationPrice, product.regular_price)
        totiShopCollapseView.setImageViewResource(R.id.imageViewNotificationImage, R.drawable.ic_toti_shop)

    }

    private fun navDeepLink(productId: Int) {
        totiShopPendingIntent = NavDeepLinkBuilder(appContext)
            .setGraph(R.navigation.graph_app)
            .setDestination(R.id.detailFragment)
            .setArguments(DetailFragmentArgs(productId).toBundle())
            .createPendingIntent()
    }

    private fun getCurrentTime(): String =
        SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss", Locale.ROOT).format(Date())

    private fun lastCheckTime(input: String): String {
        val temp = getCurrentTime()
        val splitTime = temp.split("T").component2().split(":").component1()
        return temp.replace(splitTime, (splitTime.toInt().minus(input.toInt()).toString()))
    }

    private fun notificationManagerInit() {
        totiShopNotificationManager =
            appContext.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
    }

    private fun notification() {
        totiShopNotificationChannel =
            NotificationChannel(CHANNEL_ID, DESCRIPTION, NotificationManager.IMPORTANCE_HIGH)
        totiShopNotificationManager.createNotificationChannel(totiShopNotificationChannel)

        totiShopBuilder = Notification.Builder(appContext, CHANNEL_ID)
            .setSmallIcon(R.mipmap.ic_launcher_round)
            .setCustomContentView(totiShopCollapseView)
            .setContentIntent(totiShopPendingIntent)
            .setAutoCancel(true)
            .setLargeIcon(
                BitmapFactory.decodeResource(
                    appContext.resources,
                    R.mipmap.ic_launcher_round
                )
            )

        totiShopNotificationManager.notify(NOTIFICATION_ID, totiShopBuilder.build())

    }
}