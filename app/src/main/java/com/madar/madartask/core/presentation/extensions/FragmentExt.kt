package com.madar.madartask.core.presentation.extensions

import android.Manifest
import androidx.fragment.app.Fragment
import com.madar.madartask.core.presentation.base.BaseActivity
import com.madar.madartask.core.presentation.base.PermissionCallBack
import com.madar.madartask.core.presentation.utilities.PermissionRequest
import com.madar.madartask.R

fun Fragment.grantLocationPermission(onGranted: () -> Unit, onDenied: () -> Unit = {}) {
    val locationPermission = arrayOf(Manifest.permission.ACCESS_FINE_LOCATION)

    val request = PermissionRequest(locationPermission,
        R.string.location_permissions_title,
        R.string.location_permissions_message,
        { onGranted.invoke() },
        { onDenied.invoke() }
    )
    grantPermissions(request)
}

fun Fragment.grantStoragePermission(onGranted: () -> Unit, onDenied: () -> Unit = {}) {
    val calendarPermission = arrayOf(
        Manifest.permission.CAMERA,
        Manifest.permission.WRITE_EXTERNAL_STORAGE,
        Manifest.permission.READ_EXTERNAL_STORAGE
    )

    val request = PermissionRequest(
        calendarPermission,
        onPermissionGranted = { onGranted.invoke() },
        onPermissionDenied = { onDenied.invoke() },
        title = R.string.storage_permissions_title,
        body = R.string.storage_permissions_message
    )
    grantPermissions(request)
}


private fun Fragment.grantPermissions(permissionRequest: PermissionRequest) {
    // Pair of dialog [Title and Body] message
    val dialogRes = Pair(
        permissionRequest.title ?: R.string.location_permissions_title,
        permissionRequest.body ?: R.string.location_permissions_message
    )

    // A callback to handle if the user Grant and Denied the permissions.
    // We could pass or [PermissionRequest] lambda here to handle callbacks
    val callbacks = object : PermissionCallBack {
        override fun onPermissionGranted() {
            permissionRequest.onPermissionGranted.invoke()
        }

        override fun onResultContainsDenied() {
            permissionRequest.onPermissionDenied.invoke()
        }
    }

    // Each App Activity must extend [PermissionActivity] to make this logic works
    val result = (activity as BaseActivity<*>).checkPermission(
        permissionRequest.permissions,
        callbacks,
        dialogRes
    )

    // Finally if we have already the permissions we could proceed since [checkPermission]
    // returns a [Boolean value]
    when (result) {
        true -> permissionRequest.onPermissionGranted.invoke()
        else -> {}
    }
}
