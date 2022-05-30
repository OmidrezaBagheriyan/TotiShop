package com.omidrezabagherian.totishop.data.remote

import com.omidrezabagherian.totishop.domain.model.Product
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Query
import retrofit2.http.QueryMap

interface ShopService {
    @GET
    suspend fun getProductList(
        @Query("page") page: Int,
        @Query("per_page") perPage: Int,
        @QueryMap filter: Map<String, String>
    ): Response<List<Product>>
}