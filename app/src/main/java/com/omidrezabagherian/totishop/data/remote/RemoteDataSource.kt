package com.omidrezabagherian.totishop.data.remote

import javax.inject.Inject

class RemoteDataSource @Inject constructor(private val shopService: ShopService) {
    suspend fun getProductList(page: Int, perPage: Int, filter: Map<String, String>) =
        shopService.getProductList(page, perPage, filter)
}