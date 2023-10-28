package com.nvcfitdev.stockmarketapp.data.repository

import com.nvcfitdev.stockmarketapp.data.csv.CompanyListingsParser
import com.nvcfitdev.stockmarketapp.data.local.StockDatabase
import com.nvcfitdev.stockmarketapp.data.mapper.toCompanyListing
import com.nvcfitdev.stockmarketapp.data.remote.StockService
import com.nvcfitdev.stockmarketapp.domain.model.CompanyListing
import com.nvcfitdev.stockmarketapp.domain.repository.StockRepository
import com.nvcfitdev.stockmarketapp.util.HttpResult
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import retrofit2.HttpException
import java.io.IOException
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class StockRepositoryImpl @Inject constructor(
    private val stockService: StockService,
    private val stockDatabase: StockDatabase,
    private val companyListingsParser: CompanyListingsParser
) : StockRepository() {

    private val dao = stockDatabase.dao

    companion object {
        private const val DB_SUCCESS = "db fetched"
    }

    override fun getCompanyListings(
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

            } catch (e: IOException) {
                e.printStackTrace()
                emit(HttpResult.Failed(code = -1, message = e.message, null))
            } catch (e: HttpException) {
                e.printStackTrace()
                emit(HttpResult.Failed(code = -1, message = e.message, null))
            }
        }
    }

}