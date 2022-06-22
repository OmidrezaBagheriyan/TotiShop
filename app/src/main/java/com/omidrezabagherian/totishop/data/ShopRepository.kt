package com.omidrezabagherian.totishop.data

import com.omidrezabagherian.totishop.core.safeApiCall
import com.omidrezabagherian.totishop.data.remote.RemoteDataSource
import com.omidrezabagherian.totishop.domain.model.addreview.AddReview
import com.omidrezabagherian.totishop.domain.model.createcustomer.CreateCustomer
import com.omidrezabagherian.totishop.domain.model.createorder.CreateOrder
import com.omidrezabagherian.totishop.domain.model.editreview.EditReview
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
    ) = safeApiCall {
        remoteDataSource.getProductSearchList(
            page,
            perPage,
            search,
            orderby
        )
    }

    suspend fun setCustomer(createCustomer: CreateCustomer) = safeApiCall {
        remoteDataSource.setCustomer(
            createCustomer
        )
    }

    suspend fun getCustomerByEmail(email: String) = safeApiCall {
        remoteDataSource.getCustomerByEmail(email)
    }

    suspend fun getCustomer(id: Int) = safeApiCall {
        remoteDataSource.getCustomer(id)
    }

    suspend fun setOrders(createOrder: CreateOrder) = safeApiCall {
        remoteDataSource.setOrders(
            createOrder
        )
    }

    suspend fun getOrders(id: Int) = safeApiCall {
        remoteDataSource.getOrders(id)
    }

    suspend fun addProductToOrders(id: Int, createOrder: CreateOrder) = safeApiCall {
        remoteDataSource.addProductToOrders(id, createOrder)
    }

    suspend fun editQuantityToOrders(id: Int, updateOrder: UpdateOrder) = safeApiCall {
        remoteDataSource.editQuantityToOrders(id, updateOrder)
    }

    suspend fun getCoupons(search: String) = safeApiCall {
        remoteDataSource.getCoupons(search)
    }

    suspend fun getReviews(product: Int, page: Int, perPage: Int) = safeApiCall {
        remoteDataSource.getReviews(
            product,
            page,
            perPage
        )
    }

    suspend fun getReview(id: Int) = safeApiCall {
        remoteDataSource.getReview(
            id
        )
    }

    suspend fun setReviews(addReview: AddReview) = safeApiCall {
        remoteDataSource.setReviews(
            addReview
        )
    }

    suspend fun putReviews(id: Int, addReview: EditReview) = safeApiCall {
        remoteDataSource.putReviews(
            id,
            addReview
        )
    }

    suspend fun deleteReviews(id: Int, force:Boolean) = safeApiCall {
        remoteDataSource.deleteReviews(
            id,
            force
        )
    }

}