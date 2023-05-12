package com.omidrezabagherian.totishop.domain.model.createcustomer

data class CreateCustomer(
    val billing: Billing,
    val email: String="",
    val first_name: String="",
    val last_name: String="",
    val shipping: Shipping,
    val username: String=""
)

data class Billing(
    val address_1: String = "",
    val address_2: String = "",
    val city: String = "",
    val company: String = "",
    val country: String = "",
    val email: String = "",
    val first_name: String = "",
    val last_name: String = "",
    val phone: String = "",
    val postcode: String = "",
    val state: String = ""
)

data class Shipping(
    val address_1: String = "",
    val address_2: String = "",
    val city: String = "",
    val company: String = "",
    val country: String = "",
    val first_name: String = "",
    val last_name: String = "",
    val postcode: String = "",
    val state: String = ""
)