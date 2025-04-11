package com.example.kotlinknowledge.data.repositories

import com.example.kotlinknowledge.MainApplication
import com.example.kotlinknowledge.data.remote.api.CartService
import com.example.kotlinknowledge.domain.model.CategoriesModel
import com.example.kotlinknowledge.domain.model.ProductsModel
import com.example.kotlinknowledge.data.remote.api.ProductServices
import com.example.kotlinknowledge.data.remote.mapper.RemoteErrorMapper
import com.example.kotlinknowledge.data.remote.mapper.catchingApiException
import com.example.kotlinknowledge.data.remote.requests.AddToCartRequest
import com.example.kotlinknowledge.data.remote.requests.CartProduct
import com.example.kotlinknowledge.data.remote.requests.LoginRequest
import com.example.kotlinknowledge.data.remote.responses.AddToCartResponse
import com.example.kotlinknowledge.data.remote.responses.DetailProductResponse
import com.example.kotlinknowledge.data.remote.responses.LoginResponse
import com.example.kotlinknowledge.domain.model.AppError
import com.example.kotlinknowledge.domain.model.DetailProductModel
import com.example.kotlinknowledge.domain.model.FavoriteProduct
import com.example.kotlinknowledge.domain.model.toModel
import com.example.kotlinknowledge.domain.repositories.ProductRepository
import com.github.michaelbull.result.Err
import com.github.michaelbull.result.Ok
import com.github.michaelbull.result.Result
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.withContext
import javax.inject.Inject

internal class ProductRepositoryImpl @Inject constructor(
    private val services: ProductServices,
    private val cartService: CartService,
    private val remoteErrorMapper: RemoteErrorMapper
) : ProductRepository {

    override suspend fun getProducts(limit: String, skip: String): ProductsModel {
        return services.getProducts(limit, skip)
    }

    override suspend fun getCategories(): CategoriesModel {
        return services.getCategories()
    }

    override suspend fun getDetailProduct(productId: String): Result<DetailProductModel, AppError> =
        withContext(
            Dispatchers.IO
        ) {
            catchingApiException(remoteErrorMapper) {
                services.getDetailProduct(productId).toModel()
            }
        }

    override suspend fun addToFavorite(product: FavoriteProduct) = withContext(Dispatchers.IO){
        try {
            MainApplication.db.favoriteProductDao().insertAll(product)
            return@withContext true
        }
        catch (e: Exception){
            return@withContext false
        }
    }

    override suspend fun removeFavorite(product: FavoriteProduct) = withContext(Dispatchers.IO){
        try {
            MainApplication.db.favoriteProductDao().delete(product)
            return@withContext true
        }
        catch (e: Exception){
            return@withContext false
        }
    }

    override suspend fun addToCart(userId: Int, productId: Int, quantity: Int): Flow<Result<AddToCartResponse, AppError>> = flow {
        try {
            val request = AddToCartRequest(
                userId = userId,
                products = listOf(CartProduct(id = productId, quantity = quantity))
            )
            val response = cartService.addToCart(request)
            if (response.isSuccessful) {
                response.body()?.let {
                    emit(Ok(it))
                } ?: emit(Err(AppError.UnexpectedError("Response body is null")))
            } else {
                emit(Err(AppError.ApiError("API error: ${response.code()} ${response.message()}")))
            }
        } catch (e: Exception) {
            emit(Err(AppError.UnexpectedError(e.message ?: "An unexpected error occurred")))
        }
    }

    // https://dummyjson.com/image/SIZE/BACKGROUND/COLOR
    override fun getDynamicImage(
        width: Int,
        height: Int,
        backgroundColor: String,
        text: String,
        textColor: String,
        fontSize: Int,
    ): String {
        return "https://dummyjson.com/image/${width}x${height}/${backgroundColor}/${textColor}?text=${text}&fontSize=${fontSize}"
    }
}