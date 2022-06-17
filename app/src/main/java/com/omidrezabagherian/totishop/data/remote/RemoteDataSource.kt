package com.omidrezabagherian.totishop.data.remote

import com.omidrezabagherian.totishop.domain.model.createcustomer.CreateCustomer
import com.omidrezabagherian.totishop.core.Values
import com.omidrezabagherian.totishop.domain.model.createorder.CreateOrder
import com.omidrezabagherian.totishop.domain.model.updateorder.UpdateOrder
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

    suspend fun getProduct(id: Int) = shopService.getProduct(
        id,
        Values.CUSTOMER_KEY,
        Values.CUSTOMER_SECRET
    )

    suspend fun getCategoryList(page: Int, perPage: Int) = shopService.getCategoryList(
        Values.CUSTOMER_KEY,
        Values.CUSTOMER_SECRET,
        page,
        perPage
    )

    suspend fun getProductCategoryList(page: Int, perPage: Int, category: Int) =
        shopService.getProductCategoryList(
            Values.CUSTOMER_KEY,
            Values.CUSTOMER_SECRET,
            page,
            perPage,
            category
        )

    suspend fun getProductSearchList(page: Int, perPage: Int, search: String) =
        shopService.getProductSearchList(
            Values.CUSTOMER_KEY,
            Values.CUSTOMER_SECRET,
            page,
            perPage,
            search
        )

    suspend fun setCustomer(createCustomer: CreateCustomer) = shopService.setCustomer(
        Values.CUSTOMER_KEY,
        Values.CUSTOMER_SECRET,
        createCustomer
    )


    suspend fun getCustomerByEmail(email: String) = shopService.getCustomerByEmail(
        Values.CUSTOMER_KEY,
        Values.CUSTOMER_SECRET,
        email
    )

    suspend fun getCustomer(id: Int) = shopService.getCustomer(
        id,
        Values.CUSTOMER_KEY,
        Values.CUSTOMER_SECRET
    )

    suspend fun setOrders(createOrder: CreateOrder) = shopService.setOrders(
        Values.CUSTOMER_KEY,
        Values.CUSTOMER_SECRET,
        createOrder
    )

    suspend fun getOrders(id: Int) = shopService.getOrders(
        id,
        Values.CUSTOMER_KEY,
        Values.CUSTOMER_SECRET
    )

    suspend fun addProductToOrders(id: Int, createOrder: CreateOrder) = shopService.addProductToOrders(
        id,
        Values.CUSTOMER_KEY,
        Values.CUSTOMER_SECRET,
        createOrder
    )

    suspend fun editQuantityToOrders(id: Int, updateOrder: UpdateOrder) = shopService.editQuantityToOrders(
        id,
        Values.CUSTOMER_KEY,
        Values.CUSTOMER_SECRET,
        updateOrder
    )

}