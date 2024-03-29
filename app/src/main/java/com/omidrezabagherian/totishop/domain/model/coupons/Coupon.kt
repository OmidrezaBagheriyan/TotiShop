package com.omidrezabagherian.totishop.domain.model.coupons

data class Coupon(
    val _links: Links,
    val amount: String,
    val code: String,
    val date_created: String,
    val date_created_gmt: String,
    val date_expires: Any,
    val date_expires_gmt: Any,
    val date_modified: String,
    val date_modified_gmt: String,
    val description: String,
    val discount_type: String,
    val email_restrictions: List<Any>,
    val exclude_sale_items: Boolean,
    val excluded_product_categories: List<Any>,
    val excluded_product_ids: List<Any>,
    val free_shipping: Boolean,
    val id: Int,
    val individual_use: Boolean,
    val limit_usage_to_x_items: Any,
    val maximum_amount: String,
    val meta_data: List<Any>,
    val minimum_amount: String,
    val product_categories: List<Any>,
    val product_ids: List<Any>,
    val usage_count: Int,
    val usage_limit: Any,
    val usage_limit_per_user: Any,
    val used_by: List<String>
)

data class Collection(
    val href: String
)

data class Links(
    val collection: List<Collection>,
    val self: List<Self>
)

data class Self(
    val href: String
)