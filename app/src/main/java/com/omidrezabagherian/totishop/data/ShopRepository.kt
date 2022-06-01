package com.omidrezabagherian.totishop.data

import com.omidrezabagherian.totishop.data.remote.RemoteDataSource

class ShopRepository(
    private val remoteDataSource: RemoteDataSource
) {
    suspend fun getProductList(page: Int, perPage: Int, filter: Map<String, String>) =
        remoteDataSource.getProductList(page, perPage, filter)

    suspend fun getCategoryList(page: Int, perPage: Int) =
        remoteDataSource.getCategoryList(page, perPage)
}