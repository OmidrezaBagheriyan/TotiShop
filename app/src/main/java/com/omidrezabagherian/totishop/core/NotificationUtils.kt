package com.omidrezabagherian.totishop.core

import android.annotation.SuppressLint
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.graphics.Bitmap
import androidx.core.app.NotificationCompat
import androidx.navigation.NavDeepLinkBuilder
import com.omidrezabagherian.totishop.R
import com.omidrezabagherian.totishop.core.Values.CHANNEL_ID
import com.omidrezabagherian.totishop.core.Values.NOTIFICATION_ID
import com.omidrezabagherian.totishop.ui.MainActivity
import com.omidrezabagherian.totishop.ui.details.DetailFragmentArgs

@SuppressLint("WrongConstant", "UnspecifiedImmutableFlag")
fun NotificationManager.sendNotification(
    productId:Int,
    messageBody: String,
    productImage: Bitmap?,
    applicationContext: Context
) {

    /*val contentIntent = Intent(applicationContext, MainActivity::class.java)
    val contentPendingIntent = PendingIntent.getActivity(
        applicationContext,
        NOTIFICATION_ID,
        contentIntent, PendingIntent.FLAG_UPDATE_CURRENT
    )*/

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

