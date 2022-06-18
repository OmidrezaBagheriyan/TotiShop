package com.omidrezabagherian.totishop.domain.model

data class ApiItemError(
    val message: String,
    val isFail: Boolean,
    val errorCode: Int
)
