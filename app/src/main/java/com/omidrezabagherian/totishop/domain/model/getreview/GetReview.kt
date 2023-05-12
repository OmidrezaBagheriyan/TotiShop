package com.omidrezabagherian.totishop.domain.model.getreview

data class GetReview(
    val _links: Links,
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

data class Collection(
    val href: String
)

data class Links(
    val collection: List<Collection>,
    val self: List<Self>,
    val up: List<Up>
)

data class ReviewerAvatarUrls(
    val `24`: String,
    val `48`: String,
    val `96`: String
)

data class Self(
    val href: String
)

data class Up(
    val href: String
)