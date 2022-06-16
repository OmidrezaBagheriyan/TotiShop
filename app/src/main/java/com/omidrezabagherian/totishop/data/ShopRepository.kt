package com.omidrezabagherian.totishop.data

import com.omidrezabagherian.totishop.core.Values
import com.omidrezabagherian.totishop.data.remote.RemoteDataSource
import com.omidrezabagherian.totishop.domain.model.createcustomer.CreateCustomer
import com.omidrezabagherian.totishop.domain.model.createorder.CreateOrder
import com.omidrezabagherian.totishop.domain.model.createorder.LineItem

class ShopRepository(
    private val remoteDataSource: RemoteDataSource
) {
    suspend fun getProductList(page: Int, perPage: Int, filter: Map<String, String>) =
        remoteDataSource.getProductList(page, perPage, filter)

    suspend fun getCategoryList(page: Int, perPage: Int) =
        remoteDataSource.getCategoryList(page, perPage)

    suspend fun getProduct(id: Int) = remoteDataSource.getProduct(id)

    suspend fun getProductCategoryList(page: Int, perPage: Int, category: Int) =
        remoteDataSource.getProductCategoryList(
            page,
            perPage,
            category
        )

    suspend fun getProductSearchList(page: Int, perPage: Int, search: String) =
        remoteDataSource.getProductSearchList(
            page,
            perPage,
            search
        )

    suspend fun setCustomer(createCustomer: CreateCustomer) = remoteDataSource.setCustomer(
        createCustomer
    )

    suspend fun getCustomerByEmail(email: String) = remoteDataSource.getCustomerByEmail(email)

    suspend fun getCustomer(id: Int) = remoteDataSource.getCustomer(id)

    suspend fun setOrders(createOrder: CreateOrder) = remoteDataSource.setOrders(
        createOrder
    )

    suspend fun getOrders(id: Int) = remoteDataSource.getOrders(id)

    suspend fun putOrders(id: Int, lineItem: LineItem) = remoteDataSource.putOrders(id, lineItem)

}