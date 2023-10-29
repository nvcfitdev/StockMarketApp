package com.nvcfitdev.stockmarketapp.di

import com.nvcfitdev.stockmarketapp.data.csv.CSVParser
import com.nvcfitdev.stockmarketapp.data.csv.CompanyListingsParser
import com.nvcfitdev.stockmarketapp.data.repository.StockRepositoryImpl
import com.nvcfitdev.stockmarketapp.domain.model.CompanyListing
import com.nvcfitdev.stockmarketapp.domain.repository.StockRepository
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
abstract class RepositoryModule {

    @Binds
    @Singleton
    abstract fun bindCompanyListingsParser(
        companyListingsParser: CompanyListingsParser
    ): CSVParser<CompanyListing>

    @Binds
    @Singleton
    abstract fun bindStockRepository(
        stockRepositoryImpl: StockRepositoryImpl
    ): StockRepository
}