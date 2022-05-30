package com.omidrezabagherian.totishop.data.remote

import com.omidrezabagherian.totishop.domain.model.Product
import retrofit2.Response
import retrofit2.http.*

interface ShopService {
    @GET("products")
    suspend fun getProductList(
        @Query("consumer_key") consumerKey:String,
        @Query("consumer_secret") consumerSecret:String,
        @Query("page") page: Int,
        @Query("per_page") perPage: Int,
        @QueryMap filter: Map<String, String>
    ): Response<List<Product>>
}