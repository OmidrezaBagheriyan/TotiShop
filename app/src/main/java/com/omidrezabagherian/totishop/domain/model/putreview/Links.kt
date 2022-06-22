package com.omidrezabagherian.totishop.domain.model.putreview

data class Links(
    val collection: List<Collection>,
    val reviewer: List<Reviewer>,
    val self: List<Self>,
    val up: List<Up>
)