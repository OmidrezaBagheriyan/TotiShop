package com.omidrezabagherian.totishop.data.remote

import com.omidrezabagherian.totishop.domain.model.createcustomer.CreateCustomer
import com.omidrezabagherian.totishop.domain.model.addreview.AddReview
import com.omidrezabagherian.totishop.domain.model.createorder.CreateOrder
import com.omidrezabagherian.totishop.domain.model.editreview.EditReview
import com.omidrezabagherian.totishop.domain.model.updateorder.UpdateOrder
import javax.inject.Inject

class RemoteDataSource @Inject constructor(private val shopService: ShopService) {
    suspend fun getProductList(page: Int, perPage: Int, filter: Map<String, String>) =
        shopService.getProductList(
            page,
            perPage,
            filter
        )

    suspend fun getProduct(id: Int) = shopService.getProduct(
        id
    )

    suspend fun getCategoryList(page: Int, perPage: Int) = shopService.getCategoryList(
        page,
        perPage
    )

    suspend fun getSubCategoryList(parent: Int) = shopService.getSubCategoryList(
        parent
    )

    suspend fun getProductSubCategoryList(page: Int, perPage: Int, category: Int) =
        shopService.getProductSubCategoryList(
            page,
            perPage,
            category
        )

    suspend fun getProductSearchList(
        page: Int,
        perPage: Int,
        search: String,
        orderby: String
    ) =
        shopService.getProductSearchList(
            page,
            perPage,
            search,
            orderby
        )

    suspend fun setCustomer(createCustomer: CreateCustomer) = shopService.setCustomer(
        createCustomer
    )


    suspend fun getCustomerByEmail(email: String) = shopService.getCustomerByEmail(
        email
    )

    suspend fun getCustomer(id: Int) = shopService.getCustomer(
        id
    )

    suspend fun setOrders(createOrder: CreateOrder) = shopService.setOrders(
        createOrder
    )

    suspend fun getOrders(id: Int) = shopService.getOrders(
        id
    )

    suspend fun addProductToOrders(id: Int, createOrder: CreateOrder) =
        shopService.addProductToOrders(
            id,
            createOrder
        )

    suspend fun editQuantityToOrders(id: Int, updateOrder: UpdateOrder) =
        shopService.editQuantityToOrders(
            id,
            updateOrder
        )

    suspend fun getCoupons(search: String) =
        shopService.getCoupons(
            search
        )

    suspend fun getReviews(product: Int, page: Int, perPage: Int) =
        shopService.getReviews(
            product,
            page,
            perPage
        )

    suspend fun getReview(id: Int) =
        shopService.getReview(
            id
        )

    suspend fun setReviews(addReview: AddReview) =
        shopService.setReviews(
            addReview
        )

    suspend fun putReviews(id: Int, addReview: EditReview) =
        shopService.putReviews(
            id,
            addReview
        )

    suspend fun deleteReviews(id: Int, force: Boolean) =
        shopService.deleteReviews(
            id,
            force
        )
}