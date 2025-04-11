package com.example.kotlinknowledge.data.remote.api

import com.example.kotlinknowledge.data.remote.requests.AddToCartRequest
import com.example.kotlinknowledge.data.remote.responses.AddToCartResponse
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.POST

interface CartService {
    @POST("/carts/add")
    suspend fun addToCart(@Body request: AddToCartRequest): Response<AddToCartResponse>
}