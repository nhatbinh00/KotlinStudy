package com.example.kotlinknowledge.domain.model

import com.example.kotlinknowledge.data.remote.responses.DetailProductResponse

data class DetailProductModel(
    val id: Long,
    val title: String,
    val description: String,
    val category: String,
    val price: Double,
    val discountPercentage: Double,
    val rating: Double,
    val tags: List<String>,
    val brand: String,
    val sku: String,
    val weight: Long,
    val reviews: List<Review>,
    val images: List<String>,
    val thumbnail: String,
){
    class Review(
        val rating: Long,
        val comment: String,
        val date: String,
        val reviewerName: String,
        val reviewerEmail: String,
    )
}



fun DetailProductResponse.toModel() : DetailProductModel{
    return DetailProductModel(
        id = id,
        title = title,
        description = description,
        category = category,
        price = price,
        discountPercentage = discountPercentage,
        rating = rating,
        tags = tags,
        brand = brand,
        sku = sku,
        weight = weight,
        reviews = this.reviews.map {
            DetailProductModel.Review(
                rating = it.rating,
                comment = it.comment,
                date = it.date,
                reviewerName = it.reviewerName,
                reviewerEmail = it.reviewerEmail
            )
        },
        images = images,
        thumbnail = thumbnail,
    )
}
