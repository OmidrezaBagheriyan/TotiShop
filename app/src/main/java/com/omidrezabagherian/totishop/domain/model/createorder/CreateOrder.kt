package com.omidrezabagherian.totishop.domain.model.createorder

data class CreateOrder(
    val billing: Billing,
    val line_items: List<LineItem>,
    val payment_method: String  = "",
    val payment_method_title: String,
    val set_paid: Boolean = false,
    val shipping: Shipping,
    val shipping_lines: List<ShippingLine>
)