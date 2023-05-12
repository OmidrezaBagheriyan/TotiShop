package com.omidrezabagherian.totishop.domain.model.deletereview

data class DeleteReview(
    val deleted: Boolean,
    val previous: Previous
)

data class Previous(
    val date_created: String,
    val date_created_gmt: String,
    val id: Int,
    val product_id: Int,
    val rating: Int,
    val review: String,
    val reviewer: String,
    val reviewer_avatar_urls: ReviewerAvatarUrls,
    val reviewer_email: String,
    val status: String,
    val verified: Boolean
)

data class ReviewerAvatarUrls(
    val `24`: String,
    val `48`: String,
    val `96`: String
)