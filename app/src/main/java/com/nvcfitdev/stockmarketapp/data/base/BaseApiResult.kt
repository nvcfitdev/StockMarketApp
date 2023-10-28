package com.nvcfitdev.stockmarketapp.data.base

data class BaseApiResult<K>(
    val code: Int? = -1,
    val message: String? = "",
    val data: K
)
