package com.nvcfitdev.stockmarketapp.domain.repository

import com.nvcfitdev.stockmarketapp.domain.model.CompanyInfo
import com.nvcfitdev.stockmarketapp.domain.model.CompanyListing
import com.nvcfitdev.stockmarketapp.domain.model.IntradayInfo
import com.nvcfitdev.stockmarketapp.util.BaseHttpRepository
import com.nvcfitdev.stockmarketapp.util.HttpResult
import kotlinx.coroutines.flow.Flow

abstract class StockRepository : BaseHttpRepository() {
    abstract suspend fun getCompanyListings(
        fetchFromRemote: Boolean,
        query: String
    ): Flow<HttpResult<List<CompanyListing>>>
    abstract suspend fun getIntradayInfo(
        symbol: String
    ): HttpResult<List<IntradayInfo>>
    abstract suspend  fun getCompanyInfo(
        symbol: String
    ): HttpResult<CompanyInfo>
}