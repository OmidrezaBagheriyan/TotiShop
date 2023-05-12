package com.omidrezabagherian.totishop.util

import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import com.omidrezabagherian.totishop.domain.model.util.ApiItemError
import kotlinx.coroutines.flow.flow
import retrofit2.HttpException
import retrofit2.Response
import java.io.IOException
import javax.net.ssl.SSLException

sealed class ResultWrapper<out T> {
    data class Success<out T>(val value: T) : ResultWrapper<T>()
    data class Error<out T>(val message: String?) : ResultWrapper<T>()
    object Loading : ResultWrapper<Nothing>()
}

suspend inline fun <T> safeApiCall(
    crossinline apiCall: suspend () -> Response<T>
) = flow {
    emit(ResultWrapper.Loading)
    try {
        val response = apiCall()
        val responseBody = response.body()
        if (response.isSuccessful && responseBody != null) {
            emit(ResultWrapper.Success(responseBody))
        } else {
            val errorBody = response.errorBody()
            if (errorBody != null) {
                val type = object : TypeToken<ApiItemError>() {}.type
                val responseError = Gson().fromJson<ApiItemError>(errorBody.charStream(), type)
                emit(ResultWrapper.Error(responseError.message))
            } else {
                emit(ResultWrapper.Error("Error"))
            }
        }
    } catch (exception: SSLException) {
        emit(ResultWrapper.Error(exception.message))
    } catch (exception: IOException) {
        emit(ResultWrapper.Error(exception.message))
    } catch (exception: HttpException) {
        emit(ResultWrapper.Error(exception.message))
    } catch (exception: Throwable) {
        emit(ResultWrapper.Error(exception.message))
    }
}