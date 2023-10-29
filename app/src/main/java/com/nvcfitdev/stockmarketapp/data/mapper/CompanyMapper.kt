package com.nvcfitdev.stockmarketapp.data.mapper

import com.nvcfitdev.stockmarketapp.data.local.CompanyListingEntity
import com.nvcfitdev.stockmarketapp.data.remote.dto.CompanyInfoDTO
import com.nvcfitdev.stockmarketapp.domain.model.CompanyInfo
import com.nvcfitdev.stockmarketapp.domain.model.CompanyListing

fun CompanyListingEntity.toCompanyListing(): CompanyListing =
    CompanyListing(
        name = name,
        symbol = symbol,
        exchange = exchange
    )

fun CompanyListing.toCompanyListingEntity(): CompanyListingEntity =
    CompanyListingEntity(
        name = name,
        symbol = symbol,
        exchange = exchange
    )

fun CompanyInfoDTO.toCompanyInfo(): CompanyInfo =
    CompanyInfo(
        symbol = symbol.toString(),
        description = description.toString(),
        name = name.toString(),
        country = country.toString(),
        industry = industry.toString()
    )