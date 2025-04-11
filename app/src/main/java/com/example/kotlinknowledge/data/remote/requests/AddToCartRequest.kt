package com.example.kotlinknowledge.data.remote.requests

data class AddToCartRequest(
    val userId: Int,
    val products: List<CartProduct>
)

data class CartProduct(
    val id: Int,
    val quantity: Int
)
