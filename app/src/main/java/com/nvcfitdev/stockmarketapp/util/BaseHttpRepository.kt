package com.nvcfitdev.stockmarketapp.util

import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import retrofit2.Response

open class BaseHttpRepository {

    suspend fun <K> safeApiCall(
        apiCall: suspend () -> Response<K>
    ): HttpResult<K> {
        return withContext(Dispatchers.IO) {
            try {
                apiCall.invoke().let { response ->
                    when (response.code()) {
                        in 200..299 -> {
                            HttpResult.Success(
                                code = response.code(),
                                message = response.message(),
                                data = response.body()
                            )
                        }

                        else -> HttpResult.Failed(
                            code = response.code(),
                            message = response.message(),
                            data = null
                        )
                    }
                }
            } catch (e: Throwable) {
                HttpResult.Failed(code = -1, message = e.message, data = null)
            }
        }
    }
}

sealed class HttpResult<K>(
    val code: Int? = -1,
    val message: String?,
    val data: K? = null
) {
    class Success<K>(code: Int, message: String, data: K?) : HttpResult<K>(code, message, data)
    class Failed<K>(code: Int, message: String? = "failed", data: K?) : HttpResult<K>(code, message, data)
    class Loading<K>(val isLoading: Boolean = true): HttpResult<K>(null, null, null)
}