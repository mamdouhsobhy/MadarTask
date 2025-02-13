package com.madar.madartask.core.presentation.base


interface PermissionCallBack {
    fun onPermissionGranted()

    fun onResultContainsDenied()
}