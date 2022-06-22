package com.omidrezabagherian.totishop.domain.model.addreview

data class AddReview(
    val product_id: Int,
    val rating: Double,
    val review: String,
    val reviewer: String,
    val reviewer_email: String
)