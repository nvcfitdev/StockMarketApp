package com.nvcfitdev.stockmarketapp.data.remote

import okhttp3.ResponseBody
import retrofit2.http.GET
import retrofit2.http.Query

interface StockService {
    @GET("query?function=LISTING_STATUS")
    suspend fun getListing(
        @Query("apikey") apiKey: String = API_KEY
    ): ResponseBody

    companion object {
        const val API_KEY = "3RJUFUXB0ZK9AKKL"
        const val BASE_URL = "https://alphavantage.co"
    }
}