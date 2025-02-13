package com.madar.madartask.core.data.utils

import com.google.gson.annotations.SerializedName

data class WrappedListResponse<T>(
    var code: Int,
    @SerializedName("message") var message: String,
    @SerializedName("status") var status: Boolean,
    @SerializedName("errors") var errors: List<String>? = null,
    @SerializedName("data") var data: List<T>? = null
)

data class WrappedResponse<T>(
    //var code: Int,
    @SerializedName("data") var data: T? = null,
    @SerializedName("status") var status: StatusBase,
)

data class StatusBase(
    @SerializedName("status") val status: Boolean,
    @SerializedName("code") var code: String,
    @SerializedName("messages") val message: String,
    @SerializedName("validation_message") val validation_message: String?,
)