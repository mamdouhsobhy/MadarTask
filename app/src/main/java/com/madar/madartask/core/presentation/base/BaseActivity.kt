package com.madar.madartask.core.presentation.base

import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.provider.Settings
import android.view.LayoutInflater
import android.view.View
import android.view.WindowManager
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.lifecycle.lifecycleScope
import androidx.viewbinding.ViewBinding
import com.madar.madartask.core.presentation.dialog.Dialog
import com.madar.madartask.core.presentation.dialog.ViewDialog
import com.madar.madartask.core.presentation.extensions.hideKeyboard
import com.madar.madartask.core.presentation.utilities.LocaleHelper
import com.madar.madartask.R
import com.madar.madartask.databinding.ActivityBaseBinding
import kotlinx.coroutines.launch
import java.lang.reflect.ParameterizedType

abstract class BaseActivity<VB : ViewBinding> : AppCompatActivity() {

     val binding by lazy { initBinding() }
    private val baseBinding by lazy { ActivityBaseBinding.inflate(layoutInflater) }
    lateinit var progessDialog: ViewDialog

    data class Permission(val value: String, val state: PermissionState)

    enum class PermissionState(val access: Boolean) {
        DENIED(false),
        GRANTED(true),
        UNKNOWN(false),
        NEVER_ASK(false)
    }

    val preference: SharedPreferences by lazy {
        getSharedPreferences(
            PREFS_PERMISSIONS,
            Context.MODE_PRIVATE
        )
    }

    var callBack: PermissionCallBack? = null
    var dialogComponents: Pair<Int?, Int>? = null

    @RequiresApi(Build.VERSION_CODES.JELLY_BEAN_MR1)
    override fun attachBaseContext(newBase: Context) {
        super.attachBaseContext(LocaleHelper.onAttach(newBase))
    }

    @Suppress("DEPRECATION")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        window.setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN)

        setContent()
    }

    @Suppress("UNCHECKED_CAST")
    private fun initBinding(): VB {
        val superclass = javaClass.genericSuperclass as ParameterizedType
        val method = (superclass.actualTypeArguments[0] as Class<Any>)
            .getDeclaredMethod("inflate", LayoutInflater::class.java)
        return method.invoke(null, layoutInflater) as VB
    }

    private fun setContent() {
        baseBinding.flContainer.addView(binding.root)
        setContentView(baseBinding.root)
    }

    override fun onPause() {
        hideKeyboard(currentFocus)
        super.onPause()
    }

    fun showProgress() {
        if (!this::progessDialog.isInitialized)
            progessDialog = ViewDialog(this)
        progessDialog.showDialog()
    }

    fun hidProgress() {
        progessDialog.hideDialog()
    }

    fun animateUp(view: View) {
        val animation = android.view.animation.AnimationUtils.loadAnimation(
            this,
            R.anim.slide_anim_up_slow
        )
        view.startAnimation(animation)
    }

    fun animateSideRight(view: View) {
        val animation = android.view.animation.AnimationUtils.loadAnimation(
            this,
            R.anim.enter_from_right
        )
        view.startAnimation(animation)
    }


    open fun checkPermission(
        permissions: Array<String>,
        permissionsCallBack: PermissionCallBack,
        dialogTitleAndMessage: Pair<Int?, Int>
    ): Boolean {
        callBack = permissionsCallBack
        dialogComponents = dialogTitleAndMessage

        return checkGranted(permissions)
    }

    private fun checkGranted(permissions: Array<String>): Boolean {
        // Check if there's any left permission need to be grant
        val permissionList = permissions.map { p ->
            Permission(
                p,
                getPermissionState(p)
            )
        }
            .filter { s -> !s.state.access }

        // if we don't have any permission need to be grant then return true else ask for permission
        // and return false
        return if (permissionList.isEmpty()) {
            true
        } else {
            ask(permissionList.toTypedArray())
            false
        }
    }

    private fun getPermissionState(permission: String) = convertToPermissionState(
        code = enumValueOf(preference.getString(permission, PermissionState.UNKNOWN.name) ?: PermissionState.UNKNOWN.name),
        access = ContextCompat.checkSelfPermission(
            applicationContext,
            permission
        ) == PackageManager.PERMISSION_GRANTED
    )

    private fun convertToPermissionState(code: PermissionState, access: Boolean): PermissionState {
        if (access) return PermissionState.GRANTED
        return when (code) {
            PermissionState.UNKNOWN -> PermissionState.UNKNOWN
            else -> PermissionState.DENIED
        }
    }

    private fun ask(permissions: Array<Permission>) {
        // Check if any of permissions should to show rationale
        val shouldShowRationale = permissions.any {
            it.state == PermissionState.UNKNOWN && ActivityCompat.shouldShowRequestPermissionRationale(
                this,
                it.value
            )
        }
        val shouldGoToAppSettings = permissions.any {
            it.state == PermissionState.DENIED && !ActivityCompat.shouldShowRequestPermissionRationale(
                this,
                it.value
            )
        }
        val secondTryAfterDenied = permissions.any {
            it.state == PermissionState.DENIED && ActivityCompat.shouldShowRequestPermissionRationale(
                this,
                it.value
            )
        }

        when {
            shouldShowRationale || secondTryAfterDenied -> showPermissionRationaleDialog(permissions)
            shouldGoToAppSettings -> showPermissionRationaleDialog(launchSettings = true)
            else -> requestPermissions(permissions)
        }
    }

    private fun showPermissionRationaleDialog(
        permissions: Array<Permission> = emptyArray(),
        launchSettings: Boolean = false
    ) {
        dialogComponents?.let {
            val dialog: Dialog? = Dialog.newInstance(
                it.first?.let { title -> getString(title) },
                getString(it.second),
                resources.getString(R.string.allow),
                resources.getString(R.string.cancel),
                object : Dialog.ActionListener {
                    override fun onPositiveActionClicked() {
                        if (launchSettings) openAppSettings() else requestPermissions(
                            permissions
                        )
                    }

                    override fun onNegativeActionClicked() {
                        lifecycleScope.launch {
                            permissions.forEach { p ->
                                preference.edit().putString(p.value, PermissionState.DENIED.name)
                                    .apply()
                            }
                        }
                        callBack?.onResultContainsDenied()
                    }
                })

            dialog?.show(supportFragmentManager, "dialog")
        }
    }

    private fun openAppSettings() {
        Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS).apply {
            addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
            data = Uri.fromParts("package", packageName, null)
            startActivity(this)
        }
    }

    private fun requestPermissions(permissions: Array<Permission>) {
        ActivityCompat.requestPermissions(
            this,
            permissions.map { it.value }.toTypedArray(),
            PERMISSIONS_REQUEST_CODE
        )
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        when (requestCode) {
            PERMISSIONS_REQUEST_CODE -> {
                if ((grantResults.isNotEmpty() && grantResults.all { s -> s == PackageManager.PERMISSION_GRANTED })) {
                    callBack?.onPermissionGranted()
                    lifecycleScope.launch { preference.edit().clear().apply() }
                } else {
                    callBack?.onResultContainsDenied()
                    permissions.forEach { s ->
                        lifecycleScope.launch {
                            preference.edit().putString(s, PermissionState.DENIED.name).apply()
                        }
                    }
                }
                return
            }
        }
    }

    companion object {
        private const val PREFS_PERMISSIONS = "Permissions"
        private const val PERMISSIONS_REQUEST_CODE = 777
    }

}