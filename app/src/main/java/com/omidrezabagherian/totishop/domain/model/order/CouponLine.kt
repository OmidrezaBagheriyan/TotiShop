package com.omidrezabagherian.totishop.domain.model.order

data class CouponLine(
    val code: String,
    val discount: String,
    val discount_tax: String,
    val id: Int,
    val meta_data: List<Any>
)