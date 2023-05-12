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

data class Billing(
    val address_1: String = "",
    val address_2: String = "",
    val city: String = "",
    val country: String = "",
    val email: String = "",
    val first_name: String = "",
    val last_name: String = "",
    val phone: String = "",
    val postcode: String = "",
    val state: String = ""
)

data class LineItem(
    val product_id: Int,
    val quantity: Int
)

data class Shipping(
    val address_1: String = "",
    val address_2: String = "",
    val city: String = "",
    val country: String = "",
    val first_name: String = "",
    val last_name: String = "",
    val postcode: String = "",
    val state: String = ""
)

data class ShippingLine(
    val method_id: String  = "flat_rate",
    val method_title: String = "Flat Rate",
    val total: String = "10.00"
)