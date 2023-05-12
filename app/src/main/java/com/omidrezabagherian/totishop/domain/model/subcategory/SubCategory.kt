package com.omidrezabagherian.totishop.domain.model.subcategory

data class SubCategory(
    val _links: Links,
    val count: Int,
    val description: String,
    val display: String,
    val id: Int,
    val image: Image,
    val menu_order: Int,
    val name: String,
    val parent: Int,
    val slug: String
)

data class Collection(
    val href: String
)

data class Image(
    val alt: String,
    val date_created: String,
    val date_created_gmt: String,
    val date_modified: String,
    val date_modified_gmt: String,
    val id: Int,
    val name: String,
    val src: String
)

data class Links(
    val collection: List<Collection>,
    val self: List<Self>,
    val up: List<Up>
)

data class Self(
    val href: String
)

data class Up(
    val href: String
)