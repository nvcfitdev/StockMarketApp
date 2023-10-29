package com.nvcfitdev.stockmarketapp.data.csv

import com.nvcfitdev.stockmarketapp.data.mapper.toIntradayInfo
import com.nvcfitdev.stockmarketapp.data.remote.dto.IntradayInfoDTO
import com.nvcfitdev.stockmarketapp.domain.model.CompanyListing
import com.nvcfitdev.stockmarketapp.domain.model.IntradayInfo
import com.opencsv.CSVReader
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import java.io.InputStream
import java.io.InputStreamReader
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class IntradayInfoParser @Inject constructor() : CSVParser<IntradayInfo> {

    override suspend fun parse(stream: InputStream): List<IntradayInfo> {
        val csvReader = CSVReader(InputStreamReader(stream))
        return withContext(Dispatchers.IO) {
            csvReader
                .readAll()
                .drop(1)
                .mapNotNull { line ->
                val timestamp = line.getOrNull(0) ?: return@mapNotNull null
                val close = line.getOrNull(4) ?: return@mapNotNull null
                val dto = IntradayInfoDTO(timestamp = timestamp, close = close.toDouble())
                dto.toIntradayInfo()
            }.also {
                csvReader.close()
            }
        }
    }
}