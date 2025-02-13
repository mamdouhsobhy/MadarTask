package com.madar.madartask.core.presentation.extensions

import android.content.Context
import android.content.Intent
import android.graphics.drawable.Drawable
import android.util.Log
import android.widget.ImageView
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.fragment.app.FragmentManager
import com.bumptech.glide.Glide
import com.bumptech.glide.RequestBuilder
import com.madar.madartask.R
import es.dmoral.toasty.Toasty

fun Context.showToast(message: String, connectionError: Boolean = false) {
    if (connectionError) {
        showErrorMessage(getString(R.string.check_internet_connections))
    } else {
        if(message.contains("failed to connect")){
            showErrorMessage(getString(R.string.check_internet_connections))
        }else {
            showErrorMessage(message)
        }
    }
    Log.d("error", message)
}

fun Context.showGenericAlertDialog(parentFragment: FragmentManager ? = null,code: Int = 0, message: String = "") {

    AlertDialog.Builder(this).apply {
        setMessage(message)
        setPositiveButton(getString(R.string.ok)) { dialog, _ ->
            dialog.dismiss()
        }
    }.show()

}

fun Context.showSuccessMessage(message: String = "") {
    Toasty.success(this, message, Toast.LENGTH_SHORT, true).show()
}

fun Context.showErrorMessage(message: String = "") {
    Toasty.error(this, message, Toast.LENGTH_SHORT, true).show()
}

fun Context.showInfoMessage(message: String = "") {
    Toasty.info(this, message, Toast.LENGTH_SHORT, true).show()
}