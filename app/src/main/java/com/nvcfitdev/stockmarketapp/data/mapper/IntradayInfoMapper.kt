package com.nvcfitdev.stockmarketapp.data.mapper

import com.nvcfitdev.stockmarketapp.data.remote.dto.IntradayInfoDTO
import com.nvcfitdev.stockmarketapp.domain.model.IntradayInfo
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter
import java.util.Locale

fun IntradayInfoDTO.toIntradayInfo(): IntradayInfo {
    val pattern = "yyyy-MM-dd HH:mm:ss"
    DateTimeFormatter.ofPattern(pattern, Locale.getDefault()).also { dateTimeFormatter ->
        LocalDateTime.parse(timestamp, dateTimeFormatter).also { localDateTime ->
            return IntradayInfo(
                date = localDateTime,
                close = close
            )
        }
    }
}