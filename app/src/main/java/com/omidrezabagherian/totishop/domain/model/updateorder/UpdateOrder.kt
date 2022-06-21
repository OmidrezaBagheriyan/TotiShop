package com.omidrezabagherian.totishop.domain.model.updateorder

data class UpdateOrder(
    val billing: Billing,
    val coupon_lines: List<CouponLine>,
    val line_items: List<LineItem>,
    val payment_method: String = "bacs",
    val payment_method_title: String = "Direct Bank Transfer",
    val set_paid: Boolean = true,
    val shipping: Shipping,
    val shipping_lines: List<ShippingLine>
)