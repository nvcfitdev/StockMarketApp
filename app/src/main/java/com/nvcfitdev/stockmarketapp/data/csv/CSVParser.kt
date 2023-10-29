package com.nvcfitdev.stockmarketapp.data.csv

import com.nvcfitdev.stockmarketapp.domain.model.CompanyListing
import java.io.InputStream

interface CSVParser<T>  {
    suspend fun parse(stream: InputStream): List<T>
}