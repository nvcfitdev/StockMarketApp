package com.nvcfitdev.stockmarketapp.data.remote

import com.nvcfitdev.stockmarketapp.data.remote.dto.CompanyInfoDTO
import okhttp3.ResponseBody
import retrofit2.http.GET
import retrofit2.http.Query

interface StockService {
    @GET("query?function=LISTING_STATUS")
    suspend fun getListing(
        @Query("apikey") apiKey: String = API_KEY
    ): ResponseBody
    @GET("query?function=TIME_SERIES_INTRADAY&interval=60min&datatype=csv")
    suspend fun getIntradayInfo(
        @Query("symbol") symbol: String,
        @Query("apikey") apiKey: String = API_KEY
    ): ResponseBody
    @GET("query?function=OVERVIEW")
    suspend fun getCompanyInfo(
        @Query("symbol") symbol: String,
        @Query("apikey") apiKey: String = API_KEY
    ): CompanyInfoDTO

    companion object {
        const val API_KEY = "3RJUFUXB0ZK9AKKL"
        const val BASE_URL = "https://alphavantage.co"
    }
}