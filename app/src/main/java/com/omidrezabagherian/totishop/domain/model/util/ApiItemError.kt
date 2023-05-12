package com.omidrezabagherian.totishop.domain.model.util

data class ApiItemError(
    val message: String,
    val isFail: Boolean,
    val errorCode: Int
)
