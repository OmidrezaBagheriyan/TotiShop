package com.omidrezabagherian.totishop.domain.model.updateorder

data class LineItem(
    val id: Int,
    val product_id: Int,
    val quantity: Int
)