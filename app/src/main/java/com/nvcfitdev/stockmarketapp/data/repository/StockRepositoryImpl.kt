package com.nvcfitdev.stockmarketapp.data.repository

import android.util.Log
import com.nvcfitdev.stockmarketapp.data.csv.CSVParser
import com.nvcfitdev.stockmarketapp.data.csv.IntradayInfoParser
import com.nvcfitdev.stockmarketapp.data.local.StockDatabase
import com.nvcfitdev.stockmarketapp.data.mapper.toCompanyInfo
import com.nvcfitdev.stockmarketapp.data.mapper.toCompanyListing
import com.nvcfitdev.stockmarketapp.data.mapper.toCompanyListingEntity
import com.nvcfitdev.stockmarketapp.data.remote.StockService
import com.nvcfitdev.stockmarketapp.domain.model.CompanyInfo
import com.nvcfitdev.stockmarketapp.domain.model.CompanyListing
import com.nvcfitdev.stockmarketapp.domain.model.IntradayInfo
import com.nvcfitdev.stockmarketapp.domain.repository.StockRepository
import com.nvcfitdev.stockmarketapp.util.HttpResult
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.withContext
import retrofit2.HttpException
import java.io.IOException
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class StockRepositoryImpl @Inject constructor(
    private val stockService: StockService,
    private val stockDatabase: StockDatabase,
    private val companyListingsParser: CSVParser<CompanyListing>,
    private val intradayInfoParser: CSVParser<IntradayInfo>
) : StockRepository() {

    private val dao = stockDatabase.dao

    companion object {
        private const val DB_SUCCESS = "db fetched"
    }

    override suspend fun getCompanyListings(
        fetchFromRemote: Boolean,
        query: String
    ): Flow<HttpResult<List<CompanyListing>>> {
        return flow {
            emit(HttpResult.Loading(true))
            val localListings = dao.searchCompanyListing(query)
            emit(
                (HttpResult.Success(
                    code = 200,
                    message = DB_SUCCESS,
                    data = localListings.map { it.toCompanyListing() }))
            )

            //Fetch DB
            val isDBEmpty = localListings.isNullOrEmpty() || localListings.size < 0
            val loadFromDB = !isDBEmpty && !fetchFromRemote
            if (loadFromDB) {
                emit(HttpResult.Loading(false))
                return@flow
            }

            //Fetch API
            val remoteListings = try {
                val response = stockService.getListing()
                companyListingsParser.parse(response.byteStream())
            } catch (e: IOException) {
                e.printStackTrace()
                emit(HttpResult.Failed(code = -1, message = e.message, null))
                null
            } catch (e: HttpException) {
                e.printStackTrace()
                emit(HttpResult.Failed(code = -1, message = e.message, null))
                null
            }

            //Load into DB then use data to display in presentation layer..
            remoteListings?.let { data ->
                dao.clearCompanyListings()
                dao.insertCompanyListings(data.map { it.toCompanyListingEntity() })
                emit(
                    HttpResult.Success(
                        code = 200,
                        message = "Success!",
                        dao.searchCompanyListing(query).map { it.toCompanyListing() })
                )
                emit(HttpResult.Loading(false))
            }
        }
    }

    override suspend fun getIntradayInfo(symbol: String): HttpResult<List<IntradayInfo>> {
        return try {
            val response = stockService.getIntradayInfo(symbol)
            val result = intradayInfoParser.parse(response.byteStream())
            HttpResult.Success(
                code = 200,
                message = "Success!",
                data = result
            )
        } catch (e: IOException) {
            e.printStackTrace()
            HttpResult.Failed(code = -1, message = e.message, null)
        } catch (e: HttpException) {
            e.printStackTrace()
            HttpResult.Failed(code = -1, message = e.message, null)
        }
    }

    override suspend fun getCompanyInfo(symbol: String): HttpResult<CompanyInfo> {
        return try {
            val result = stockService.getCompanyInfo(symbol)
            HttpResult.Success(
                code = 200,
                message = "Success",
                data = result.toCompanyInfo()
            )
        } catch (e: IOException) {
            e.printStackTrace()
            HttpResult.Failed(code = -1, message = e.message, null)
        } catch (e: HttpException) {
            e.printStackTrace()
            HttpResult.Failed(code = -1, message = e.message, null)
        }
    }

}