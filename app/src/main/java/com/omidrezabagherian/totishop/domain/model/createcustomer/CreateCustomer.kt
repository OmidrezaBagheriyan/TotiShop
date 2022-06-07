package com.omidrezabagherian.totishop.domain.model.createcustomer

data class CreateCustomer(
    val billing: Billing,
    val email: String,
    val first_name: String,
    val last_name: String,
    val shipping: Shipping,
    val username: String
)