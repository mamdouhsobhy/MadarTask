package com.madar.madartask.core.presentation.extensions

import android.net.Uri
import android.os.Bundle
import androidx.annotation.IdRes
import androidx.fragment.app.Fragment
import androidx.navigation.NavController
import androidx.navigation.NavDirections
import androidx.navigation.NavOptions
import androidx.navigation.Navigator
import androidx.navigation.fragment.findNavController

fun <T> Fragment.getNavigationResult(key: String = "result") =
    findNavController().currentBackStackEntry?.savedStateHandle?.get<T>(key)

fun <T> Fragment.getNavigationResultLiveData(key: String = "result") =
    findNavController().currentBackStackEntry?.savedStateHandle?.getLiveData<T>(key)

fun <T> Fragment.setNavigationResult(result: T, key: String = "result") {
    findNavController().previousBackStackEntry?.savedStateHandle?.set(key, result)
}

/**
 * Call this method after you retrieve your result using the previous method @getNavigationResultLiveData
 */
fun <T> Fragment.clearNavigationResult(key: String = "result") {
    findNavController().currentBackStackEntry?.savedStateHandle?.remove<T>(key)
}

/**
 * Navigates only if this is safely possible; when this Fragment is still the current destination.
 * todo this function not support [Global Action] see more about global action: {https://developer.android.com/guide/navigation/navigation-global-action}
 */
fun NavController.safeNavigate(
    navDirections: NavDirections
) {
    currentDestination?.getAction(navDirections.actionId)
        ?.let { navigate(navDirections) }
}

/**
 * Navigates only if this is safely possible; when this Fragment is still the current destination.
 * todo this function not support [Global Action] see more about global action: {https://developer.android.com/guide/navigation/navigation-global-action}
 */
fun NavController.safeNavigate(
    @IdRes resId: Int,
    args: Bundle? = null,
    navOptions: NavOptions? = null,
    navigatorExtras: Navigator.Extras? = null
) {
    currentDestination?.getAction(resId)?.let {
        navigate(
            resId, args,
            navOptions, navigatorExtras
        )
    }
}

/**
 * Navigates using deep link uri only if this is safely possible;
 */
fun NavController.safeNavigate(
    deepLink: Uri
) {
    if (graph.hasDeepLink(deepLink))
        navigate(deepLink)
}

fun NavController.safeNavigate(
    deepLink: Uri,
    navOptions: NavOptions? = null,
    navigatorExtras: Navigator.Extras? = null
) {
    if (graph.hasDeepLink(deepLink))
        navigate(deepLink, navOptions, navigatorExtras)
}

