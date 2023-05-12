package com.omidrezabagherian.totishop.util

import android.content.SharedPreferences

object Values {
    const val BASE_URL = "https://woocommerce.maktabsharif.ir/wp-json/wc/v3/"
    const val CUSTOMER_KEY = "ck_63f4c52da932ddad1570283b31f3c96c4bd9fd6f"
    const val CUSTOMER_SECRET = "cs_294e7de35430398f323b43c21dd1b29f67b5370b"
    const val DELAY_MS: Long = 2000
    const val PERIOD_MS: Long = 3000
    const val SHARED_PREFERENCES = "TotiApp"
    const val ID_SHARED_PREFERENCES = "ID"
    const val NAME_SHARED_PREFERENCES = "Name"
    const val FAMILY_SHARED_PREFERENCES = "Family"
    const val EMAIL_SHARED_PREFERENCES = "Email"
    const val Address_SHARED_PREFERENCES = "Address"
    const val PASSWORD_SHARED_PREFERENCES = "Password"
    const val ID_ORDER_SHARED_PREFERENCES = "IDOrder"
    const val IS_ORDER_ENABLE_SHARED_PREFERENCE = "IsOrderEnable"
    const val ID_FILTER_SHARED_PREFERENCES = "IDFilter"
    const val ID_LAST_PRODUCT_SHARED_PREFERENCES = "IDLastProduct"
    const val ID_TIME_WORK_SHARED_PREFERENCES = "IDTime"
    const val CHANNEL_ID = "TotiShopNotification"
    const val NOTIFICATION_ID = 0
    const val DATA_NAME = "TimeWorkManager"
    lateinit var mainSharedPreferences: SharedPreferences
}