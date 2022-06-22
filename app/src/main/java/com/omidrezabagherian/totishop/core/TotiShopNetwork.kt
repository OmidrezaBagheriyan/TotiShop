package com.omidrezabagherian.totishop.core

import com.omidrezabagherian.totishop.core.Values.BASE_URL
import com.omidrezabagherian.totishop.data.remote.ShopService
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

object TotiShopNetwork {
    private val retrofit =
        Retrofit.Builder().baseUrl(BASE_URL).addConverterFactory(GsonConverterFactory.create())
            .build()

    val shopService: ShopService = retrofit.create(ShopService::class.java)
}