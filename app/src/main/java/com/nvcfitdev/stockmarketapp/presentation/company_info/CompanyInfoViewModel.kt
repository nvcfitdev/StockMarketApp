package com.nvcfitdev.stockmarketapp.presentation.company_info

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.nvcfitdev.stockmarketapp.domain.model.CompanyInfo
import com.nvcfitdev.stockmarketapp.domain.model.IntradayInfo
import com.nvcfitdev.stockmarketapp.domain.repository.StockRepository
import com.nvcfitdev.stockmarketapp.util.HttpResult
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Deferred
import kotlinx.coroutines.async
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class CompanyInfoViewModel @Inject constructor(
    private val savedStateHandle: SavedStateHandle,
    private val repository: StockRepository
): ViewModel() {

    var state by mutableStateOf(CompanyInfoState())

    init {
        viewModelScope.launch {
            val symbol = savedStateHandle.get<String>("symbol") ?: return@launch
            state = state.copy(isLoading = true)
            val companyInfoResult = async { repository.getCompanyInfo(symbol) }.await()
            val intradayInfoResult = async { repository.getIntradayInfo(symbol) }.await()
            getCompanyInfo(companyInfoResult)
            getIntradayInfo(intradayInfoResult)
        }
    }

    private fun getCompanyInfo(companyInfoResult: HttpResult<CompanyInfo>){
        when (val result = companyInfoResult) {
            is HttpResult.Success -> {
                state = state.copy(
                    company = result.data,
                    isLoading = false,
                    error = null
                )
            }
            is HttpResult.Failed -> {
                state = state.copy(
                    isLoading = false,
                    error = result.message,
                    company = null
                )
            }
            else -> Unit
        }
    }

    private fun getIntradayInfo(intradayInfoResult: HttpResult<List<IntradayInfo>>){
        when (val result = intradayInfoResult) {
            is HttpResult.Success -> {
                state = state.copy(
                    stockInfos = result.data ?: emptyList(),
                    isLoading = false,
                    error = null
                )
            }
            is HttpResult.Failed -> {
                state = state.copy(
                    isLoading = false,
                    error = result.message,
                    stockInfos = emptyList()
                )
            }
            else -> Unit
        }
    }

}