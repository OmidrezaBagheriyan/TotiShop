package com.omidrezabagherian.totishop.util

import android.annotation.SuppressLint
import android.app.NotificationManager
import android.content.Context
import android.graphics.Bitmap
import androidx.core.app.NotificationCompat
import androidx.navigation.NavDeepLinkBuilder
import com.omidrezabagherian.totishop.R
import com.omidrezabagherian.totishop.util.Values.CHANNEL_ID
import com.omidrezabagherian.totishop.util.Values.NOTIFICATION_ID
import com.omidrezabagherian.totishop.ui.details.DetailFragmentArgs

@SuppressLint("WrongConstant", "UnspecifiedImmutableFlag")
fun NotificationManager.sendNotification(
    productId:Int,
    messageBody: String,
    productImage: Bitmap?,
    applicationContext: Context
) {

    val contentPendingIntent = NavDeepLinkBuilder(applicationContext)
        .setGraph(R.navigation.graph_app)
        .setDestination(R.id.detailFragment)
        .setArguments(DetailFragmentArgs(productId).toBundle())
        .createPendingIntent()

    val bigPicStyle = NotificationCompat.BigPictureStyle()
        .bigPicture(productImage)
        .bigLargeIcon(null)


    val builder = NotificationCompat.Builder(
        applicationContext,
        CHANNEL_ID
    )
        .setSmallIcon(R.drawable.ic_toti_shop)
        .setContentTitle("کالای جدید در طوطی شاپ")
        .setContentText(messageBody)
        .setContentIntent(contentPendingIntent)
        .setAutoCancel(true)
        .setStyle(bigPicStyle)
        .setLargeIcon(productImage)
        .setPriority(NotificationCompat.PRIORITY_HIGH)


    notify(NOTIFICATION_ID, builder.build())

}

