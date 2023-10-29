package com.nvcfitdev.stockmarketapp.data.remote.dto

import com.google.gson.annotations.SerializedName

data class IntradayInfoDTO(
    @SerializedName("Timestamp") val timestamp: String,
    @SerializedName("Close") val close: Double
)
