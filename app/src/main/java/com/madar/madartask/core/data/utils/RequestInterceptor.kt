package com.madar.madartask.core.data.utils

import com.madar.madartask.core.presentation.common.SharedPrefs
import okhttp3.Interceptor
import okhttp3.Response

class RequestInterceptor constructor(private val pref: SharedPrefs) : Interceptor {
    override fun intercept(chain: Interceptor.Chain): Response {

        val authToken = "Bearer " + pref.getToken()
        val newRequest = chain.request().newBuilder()
            .addHeader("Accept", "application/json")
            .addHeader("Content-Type", "application/json")
            .addHeader(Constant.Authorization, authToken)
            .build()
        return chain.proceed(newRequest)
    }
}