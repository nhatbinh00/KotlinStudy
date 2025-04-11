package com.example.kotlinknowledge.domain.repositories

import com.example.kotlinknowledge.data.remote.requests.LoginRequest
import com.example.kotlinknowledge.data.remote.responses.AddToCartResponse
import com.example.kotlinknowledge.data.remote.responses.DetailProductResponse
import com.example.kotlinknowledge.data.remote.responses.LoginResponse
import com.example.kotlinknowledge.domain.model.AppError
import com.example.kotlinknowledge.domain.model.CategoriesModel
import com.example.kotlinknowledge.domain.model.DetailProductModel
import com.example.kotlinknowledge.domain.model.FavoriteProduct
import com.example.kotlinknowledge.domain.model.ProductsModel
import com.github.michaelbull.result.Result
import kotlinx.coroutines.flow.Flow



interface ProductRepository {
    suspend fun getProducts(limit: String, skip: String): ProductsModel

    suspend fun getCategories(): CategoriesModel

    suspend fun getDetailProduct(productId: String): Result<DetailProductModel, AppError>

    suspend fun addToFavorite(product: FavoriteProduct): Boolean

    suspend fun removeFavorite(product: FavoriteProduct): Boolean

    suspend fun addToCart(userId: Int, productId: Int, quantity: Int): Flow<Result<AddToCartResponse, AppError>>

    // https://dummyjson.com/image/SIZE/BACKGROUND/COLOR
    fun getDynamicImage(
        width: Int = 1920,
        height: Int = 1080,
        backgroundColor: String = "eb9153",
        text: String = "Hello World",
        textColor: String = "ffffff",
        fontSize: Int = 16,
    ): String
}