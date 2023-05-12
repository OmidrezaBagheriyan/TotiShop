package com.omidrezabagherian.totishop.domain.model.customer

data class Customer(
    val _links: Links,
    val avatar_url: String,
    val billing: Billing,
    val date_created: String,
    val date_created_gmt: String,
    val date_modified: String,
    val date_modified_gmt: String,
    val email: String,
    val first_name: String,
    val id: Int,
    val is_paying_customer: Boolean,
    val last_name: String,
    val meta_data: List<Any>,
    val role: String,
    val shipping: Shipping,
    val username: String
)

data class Billing(
    val address_1: String,
    val address_2: String,
    val city: String,
    val company: String,
    val country: String,
    val email: String,
    val first_name: String,
    val last_name: String,
    val phone: String,
    val postcode: String,
    val state: String
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

data class Shipping(
    val address_1: String,
    val address_2: String,
    val city: String,
    val company: String,
    val country: String,
    val first_name: String,
    val last_name: String,
    val postcode: String,
    val state: String
)