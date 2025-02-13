package com.madar.madartask.core.presentation.utilities

import android.annotation.SuppressLint
import android.content.Intent
import android.location.Location
import android.net.Uri
import android.provider.Settings.ACTION_APPLICATION_DETAILS_SETTINGS
import androidx.annotation.NonNull
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.MutableLiveData


object Locator {

  private const val TAG = "Locator"
  const val REQUEST_CHECK_SETTINGS = 1000
  const val REQUEST_CANCEL_SETTINGS = 2000
  private const val UPDATE_INTERVAL = 3000L
  private const val FASTEST_INTERVAL = UPDATE_INTERVAL / 2
  private const val SMALLEST_DISPLACEMENT = 200F
  private lateinit var activity: AppCompatActivity
  private val lastLocation = MutableLiveData<Location>()
  private var locationCallbackAttached = false


  /**
   * Determine as precise a location as possible from the available location providers, GPS as well as WiFi
   * and mobile cell data. if the caller has location permission, otherwise it will ask user for permission.
   *
   * @param activity The target activity.
   * @param requireLocationSettings Indicates whether location settings is required or not.
   * @param onSuccess The call back to receive the location.
   */
  /**
   * Listen to location updates.
   * Don't forget to call stopLocationUpdates() when no more required.
   *
   * @param activity The target activity.
   * @param requireLocationSettings Indicates whether location settings is required or not.
   * @param onLocationChanged The call back to receive the updated location.
   */


  /**
   * Checks if location is enabled, if not, opens a dialog asking to open it.
   * Don't forget to check the result in onActivityResult() with request code REQUEST_CHECK_SETTINGS.
   *
   * @param activity The target activity.
   * @param onCheckedCompleted The call back when check completed.
   */

  /**
   * Stops location updates when no more required.
   */

  /**
   * Initialize location provider client if it not before.
   *
   * @param activity The target activity.
   */

  @SuppressLint("MissingPermission")
  /**
   * The fused Location Provider will only maintain background location if at least one client is
   * connected to it. But we don't want to launch the Maps app to get last location, and also we can't say
   * our users to launch Maps app to get last location.
   * What we need to do is request location updates once it get location and stop it.
   *
   * @param activity The target activity.
   * @param onLocationReturned The The call back when location returned.
   */

  /**
   * Listen to location changes.
   *
   * @param activity The target activity.
   * @param onLocationChanged The call back which triggered every time location changed.
   */

  /**
   * Show settings alert dialog.
   *
   * @param activity The target activity.
   */
  private fun showSettingsAlert(@NonNull activity: AppCompatActivity) {
          openSettings(activity)
  }

  /**
   * Navigating user to app settings
   *
   * @param activity The target activity.
   */
  private fun openSettings(@NonNull activity: AppCompatActivity) {
    Intent(ACTION_APPLICATION_DETAILS_SETTINGS).apply {
      data = Uri.fromParts("package", activity.packageName, null)
      activity.startActivity(this)
    }
  }
}