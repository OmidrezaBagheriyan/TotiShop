package com.omidrezabagherian.totishop.data

import com.omidrezabagherian.totishop.core.safeApiCall
import com.omidrezabagherian.totishop.data.remote.RemoteDataSource
import com.omidrezabagherian.totishop.domain.model.createcustomer.CreateCustomer
import com.omidrezabagherian.totishop.domain.model.createorder.CreateOrder
import com.omidrezabagherian.totishop.domain.model.updateorder.UpdateOrder
import kotlinx.coroutines.CoroutineDispatcher

class ShopRepository(
    private val remoteDataSource: RemoteDataSource,
    private val dispatcher: CoroutineDispatcher
) {
    suspend fun getProductList(page: Int, perPage: Int, filter: Map<String, String>) = safeApiCall {
        remoteDataSource.getProductList(page, perPage, filter)
    }

    suspend fun getCategoryList(page: Int, perPage: Int) = safeApiCall {
        remoteDataSource.getCategoryList(page, perPage)
    }

    suspend fun getSubCategoryList(parent: Int) = safeApiCall {
        remoteDataSource.getSubCategoryList(parent)
    }

    suspend fun getProduct(id: Int) = safeApiCall {
        remoteDataSource.getProduct(id)
    }

    suspend fun getProductSubCategoryList(page: Int, perPage: Int, category: Int) = safeApiCall {
        remoteDataSource.getProductSubCategoryList(
            page,
            perPage,
            category
        )
    }

    suspend fun getProductSearchList(
        page: Int,
        perPage: Int,
        search: String,
        orderby: String
    ) =
        remoteDataSource.getProductSearchList(
            page,
            perPage,
            search,
            orderby
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

    suspend fun addProductToOrders(id: Int, createOrder: CreateOrder) =
        remoteDataSource.addProductToOrders(id, createOrder)

    suspend fun editQuantityToOrders(id: Int, updateOrder: UpdateOrder) =
        remoteDataSource.editQuantityToOrders(id, updateOrder)

}