package com.example.kotlinknowledge.data.remote.responses

data class AddToCartResponse(
    val id: Int,
    val products: List<CartItem>,
    val total: Int,
    val discountedTotal: Int,
    val userId: Int,
    val totalProducts: Int,
    val totalQuantity: Int
)

data class CartItem(
    val id: Int,
    val title: String,
    val price: Int,
    val quantity: Int,
    val discountPercentage: Double,
    val discountedPrice: Int,
    val thumbnail: String
)