package com.madar.madartask.core.presentation.utilities

/**
 * A Simple POJO to hold required fields to be pass to [BaseFragmentHilt] to make app ask for
 * required permissions with a simple way.
 */
data class PermissionRequest(
    val permissions: Array<String>,
    val title: Int? = null,
    val body: Int? = null,
    val onPermissionGranted: () -> Unit,
    val onPermissionDenied: () -> Unit
) {
    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false

        other as PermissionRequest

        if (!permissions.contentEquals(other.permissions)) return false
        if (title != other.title) return false
        if (body != other.body) return false
        if (onPermissionGranted != other.onPermissionGranted) return false
        if (onPermissionDenied != other.onPermissionDenied) return false

        return true
    }

    override fun hashCode(): Int {
        var result = permissions.contentHashCode()
        result = 31 * result + (title?.hashCode() ?: 0)
        result = 31 * result + (body?.hashCode() ?: 0)
        result = 31 * result + onPermissionGranted.hashCode()
        result = 31 * result + onPermissionDenied.hashCode()
        return result
    }
}