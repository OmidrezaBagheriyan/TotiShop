package com.omidrezabagherian.totishop.data.remote

import com.omidrezabagherian.totishop.domain.model.category.Category
import com.omidrezabagherian.totishop.domain.model.createcustomer.CreateCustomer
import com.omidrezabagherian.totishop.domain.model.customer.Customer
import com.omidrezabagherian.totishop.domain.model.product.Product
import retrofit2.Response
import retrofit2.http.*

interface ShopService {
    @GET("products")
    suspend fun getProductList(
        @Query("consumer_key") consumerKey: String,
        @Query("consumer_secret") consumerSecret: String,
        @Query("page") page: Int,
        @Query("per_page") perPage: Int,
        @QueryMap filter: Map<String, String>
    ): Response<List<Product>>

    @GET("products/{id}")
    suspend fun getProduct(
        @Path("id") id: Int,
        @Query("consumer_key") consumerKey: String,
        @Query("consumer_secret") consumerSecret: String
    ): Response<Product>

    @GET("products/categories")
    suspend fun getCategoryList(
        @Query("consumer_key") consumerKey: String,
        @Query("consumer_secret") consumerSecret: String,
        @Query("page") page: Int,
        @Query("per_page") perPage: Int
    ): Response<List<Category>>

    @GET("products")
    suspend fun getProductCategoryList(
        @Query("consumer_key") consumerKey: String,
        @Query("consumer_secret") consumerSecret: String,
        @Query("page") page: Int,
        @Query("per_page") perPage: Int,
        @Query("category") category: Int
    ): Response<List<Product>>

    @GET("products")
    suspend fun getProductSearchList(
        @Query("consumer_key") consumerKey: String,
        @Query("consumer_secret") consumerSecret: String,
        @Query("page") page: Int,
        @Query("per_page") perPage: Int,
        @Query("search") search: String
    ): Response<List<Product>>

    @POST("customers")
    suspend fun setCustomer(
        @Query("consumer_key") consumerKey: String,
        @Query("consumer_secret") consumerSecret: String,
        @Body createCustomer: CreateCustomer
    ): Response<Customer>

    @GET("customers")
    suspend fun getCustomerByEmail(
        @Query("consumer_key") consumerKey: String,
        @Query("consumer_secret") consumerSecret: String,
        @Query("email") email: String
    ): Response<List<Customer>>

    @GET("customers/{id}")
    suspend fun getCustomer(
        @Path("id") id: Int,
        @Query("consumer_key") consumerKey: String,
        @Query("consumer_secret") consumerSecret: String
    ): Response<Customer>
}