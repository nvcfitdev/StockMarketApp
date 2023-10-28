package com.nvcfitdev.stockmarketapp.domain.repository

import com.nvcfitdev.stockmarketapp.domain.model.CompanyListing
import com.nvcfitdev.stockmarketapp.util.BaseHttpRepository
import com.nvcfitdev.stockmarketapp.util.HttpResult
import kotlinx.coroutines.flow.Flow

abstract class StockRepository : BaseHttpRepository() {
    abstract fun getCompanyListings(
        fetchFromRemote: Boolean,
        query: String
    ): Flow<HttpResult<List<CompanyListing>>>
}