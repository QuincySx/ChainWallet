package com.smallraw.chain.eos.api.utils

import com.smallraw.chain.eos.api.exception.ApiError
import com.smallraw.chain.eos.api.exception.ApiException
import okhttp3.OkHttpClient
import retrofit2.Call
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.jackson.JacksonConverterFactory
import java.io.IOException

object Generator {
    private val httpClient by lazy {
        OkHttpClient.Builder()
    }
    private val builder by lazy {
        Retrofit.Builder()
            .addConverterFactory(JacksonConverterFactory.create())
    }
    private var retrofit: Retrofit? = null

    fun <S> createService(serviceClass: Class<S>, baseUrl: String): S {
        builder.baseUrl(baseUrl)
        builder.client(httpClient.build())
        builder.addConverterFactory(JacksonConverterFactory.create())
        retrofit = builder.build()
        return retrofit!!.create(serviceClass)
    }

    fun <T> executeSync(call: Call<T>): T? {
        return try {
            val response = call.execute()
            if (response.isSuccessful) {
                response.body()
            } else {
                val apiError = getApiError(response)
                throw ApiException(apiError)
            }
        } catch (e: IOException) {
            throw ApiException(e)
        }
    }

    @Throws(IOException::class, ApiException::class)
    private fun getApiError(response: Response<*>): ApiError? {
        return retrofit!!.responseBodyConverter<Any>(
            ApiError::class.java, arrayOfNulls(0)
        ).convert(response.errorBody()) as ApiError?
    }
}