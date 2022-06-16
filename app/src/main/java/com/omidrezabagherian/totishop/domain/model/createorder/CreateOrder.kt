package com.omidrezabagherian.totishop.domain.model.createorder

data class CreateOrder(
    val billing: Billing,
    val line_items: List<LineItem>,
    val payment_method: String = "bacs",
    val payment_method_title: String = "Direct Bank Transfer",
    val set_paid: Boolean = true,
    val shipping: Shipping,
    val shipping_lines: List<ShippingLine>
)