package com.omidrezabagherian.totishop.domain.model.createorder

data class ShippingLine(
    val method_id: String  = "flat_rate",
    val method_title: String = "Flat Rate",
    val total: String = "10.00"
)