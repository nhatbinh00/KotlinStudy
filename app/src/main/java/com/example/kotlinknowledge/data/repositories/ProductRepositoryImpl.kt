package com.example.kotlinknowledge.data.repositories

import com.example.kotlinknowledge.domain.model.CategoriesModel
import com.example.kotlinknowledge.domain.model.ProductsModel
import com.example.kotlinknowledge.data.remote.api.ProductServices
import com.example.kotlinknowledge.data.remote.mapper.RemoteErrorMapper
import com.example.kotlinknowledge.data.remote.mapper.catchingApiException
import com.example.kotlinknowledge.data.remote.requests.LoginRequest
import com.example.kotlinknowledge.data.remote.responses.DetailProductResponse
import com.example.kotlinknowledge.data.remote.responses.LoginResponse
import com.example.kotlinknowledge.domain.model.AppError
import com.example.kotlinknowledge.domain.model.DetailProductModel
import com.example.kotlinknowledge.domain.model.toModel
import com.example.kotlinknowledge.domain.repositories.ProductRepository
import com.github.michaelbull.result.Result
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import javax.inject.Inject

internal class ProductRepositoryImpl @Inject constructor(
    private val services: ProductServices,
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