package com.omidrezabagherian.totishop.data.remote

import com.omidrezabagherian.totishop.util.Values
import com.omidrezabagherian.totishop.domain.model.addreview.AddReview
import com.omidrezabagherian.totishop.domain.model.category.Category
import com.omidrezabagherian.totishop.domain.model.coupons.Coupon
import com.omidrezabagherian.totishop.domain.model.createcustomer.CreateCustomer
import com.omidrezabagherian.totishop.domain.model.createorder.CreateOrder
import com.omidrezabagherian.totishop.domain.model.customer.Customer
import com.omidrezabagherian.totishop.domain.model.deletereview.DeleteReview
import com.omidrezabagherian.totishop.domain.model.editreview.EditReview
import com.omidrezabagherian.totishop.domain.model.order.Order
import com.omidrezabagherian.totishop.domain.model.product.Product
import com.omidrezabagherian.totishop.domain.model.review.Review
import com.omidrezabagherian.totishop.domain.model.getreview.GetReview
import com.omidrezabagherian.totishop.domain.model.putreview.PutReview
import com.omidrezabagherian.totishop.domain.model.subcategory.SubCategory
import com.omidrezabagherian.totishop.domain.model.updateorder.UpdateOrder
import retrofit2.Response
import retrofit2.http.*

interface ShopService {
    @GET("products")
    suspend fun getProductList(
        @Query("page") page: Int,
        @Query("per_page") perPage: Int,
        @QueryMap filter: Map<String, String>
    ): Response<List<Product>>

    @GET("products/{id}")
    suspend fun getProduct(
        @Path("id") id: Int
    ): Response<Product>

    @GET("products/categories")
    suspend fun getCategoryList(
        @Query("page") page: Int,
        @Query("per_page") perPage: Int
    ): Response<List<Category>>

    @GET("products/categories")
    suspend fun getSubCategoryList(
        @Query("parent") parent: Int
    ): Response<List<SubCategory>>

    @GET("products")
    suspend fun getProductSubCategoryList(
        @Query("page") page: Int,
        @Query("per_page") perPage: Int,
        @Query("category") category: Int
    ): Response<List<Product>>

    @GET("products")
    suspend fun getProductSearchList(
        @Query("page") page: Int,
        @Query("per_page") perPage: Int,
        @Query("search") search: String,
        @Query("orderby") orderby: String
    ): Response<List<Product>>

    @POST("customers")
    suspend fun setCustomer(
        @Body createCustomer: CreateCustomer
    ): Response<Customer>

    @GET("customers")
    suspend fun getCustomerByEmail(
        @Query("email") email: String
    ): Response<List<Customer>>

    @GET("customers/{id}")
    suspend fun getCustomer(
        @Path("id") id: Int
    ): Response<Customer>

    @POST("orders")
    suspend fun setOrders(
        @Body createOrder: CreateOrder
    ): Response<Order>

    @GET("orders/{id}")
    suspend fun getOrders(
        @Path("id") id: Int
    ): Response<Order>

    @PUT("orders/{id}")
    suspend fun addProductToOrders(
        @Path("id") id: Int,
        @Body createOrder: CreateOrder
    ): Response<Order>

    @PUT("orders/{id}")
    suspend fun editQuantityToOrders(
        @Path("id") id: Int,
        @Body updateOrder: UpdateOrder
    ): Response<Order>

    @GET("coupons")
    suspend fun getCoupons(
        @Query("search") search: String
    ): Response<List<Coupon>>

    @GET("products/reviews")
    suspend fun getReviews(
        @Query("product") product: Int,
        @Query("page") page: Int,
        @Query("per_page") perPage: Int
    ): Response<List<Review>>

    @GET("products/reviews/{id}")
    suspend fun getReview(
        @Path("id") id: Int
    ): Response<GetReview>

    @POST("products/reviews")
    suspend fun setReviews(
        @Body addReview: AddReview
    ): Response<Review>

    @PUT("products/reviews/{id}")
    suspend fun putReviews(
        @Path("id") id: Int,
        @Body addReview: EditReview
    ): Response<PutReview>

    @DELETE("products/reviews/{id}")
    suspend fun deleteReviews(
        @Path("id") id: Int,
        @Query("force")force:Boolean
    ):Response<DeleteReview>

    @GET("products")
    suspend fun getNewProductList(
        @Query("after") after: String
    ): Response<List<Product>>
}