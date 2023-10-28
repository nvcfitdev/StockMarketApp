package com.nvcfitdev.stockmarketapp.data.mapper

import com.nvcfitdev.stockmarketapp.data.local.CompanyListingEntity
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