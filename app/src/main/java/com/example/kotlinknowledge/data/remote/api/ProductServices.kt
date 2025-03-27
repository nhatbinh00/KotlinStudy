package com.example.kotlinknowledge.data.remote.api

import com.example.kotlinknowledge.data.remote.responses.DetailProductResponse
import com.example.kotlinknowledge.domain.model.CategoriesModel
import com.example.kotlinknowledge.domain.model.ProductsModel
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

interface ProductServices {
    @GET("products")
    suspend fun getProducts(
        @Query("limit")  sort: String,
        @Query("skip")  skip: String,
    ): ProductsModel

    @GET("products/categories")
    suspend fun getCategories(): CategoriesModel

    @GET("products/{productId}")
    suspend fun getDetailProduct(
        @Path("productId") productId: String,
    ): DetailProductResponse
}