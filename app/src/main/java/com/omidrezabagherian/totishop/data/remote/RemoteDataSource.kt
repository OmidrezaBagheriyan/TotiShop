package com.omidrezabagherian.totishop.data.remote

import com.omidrezabagherian.totishop.util.Values
import javax.inject.Inject

class RemoteDataSource @Inject constructor(private val shopService: ShopService) {
    suspend fun getProductList(page: Int, perPage: Int, filter: Map<String, String>) =
        shopService.getProductList(
            Values.CUSTOMER_KEY,
            Values.CUSTOMER_SECRET,
            page,
            perPage,
            filter
        )

    suspend fun getCategoryList(page: Int, perPage: Int) = shopService.getCategoryList(
        Values.CUSTOMER_KEY,
        Values.CUSTOMER_SECRET,
        page,
        perPage
    )
}