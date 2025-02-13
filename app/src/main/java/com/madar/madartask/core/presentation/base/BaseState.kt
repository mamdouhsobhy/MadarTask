package com.madar.madartask.core.presentation.base

sealed class BaseState<out T> {

    object Init : BaseState<Nothing>()

    data class IsLoading(val isLoading: Boolean) : BaseState<Nothing>()

    data class ShowToast(val message: String, val isConnectionError: Boolean) :
        BaseState<Nothing>()

    data class Success<T : Any>(val items: T?) : BaseState<T>()

    data class Error(val code: String, val message: String) : BaseState<Nothing>()
}