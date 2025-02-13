package com.madar.madartask.app

import android.app.Application
import com.madar.madartask.core.presentation.common.SharedPrefs
import com.madar.madartask.core.presentation.utilities.LocaleHelper
import dagger.hilt.android.HiltAndroidApp
import javax.inject.Inject

@HiltAndroidApp
class MadarApplication : Application() {

    @Inject
    lateinit var sharedPrefs: SharedPrefs

    override fun onCreate() {
        super.onCreate()
        LocaleHelper.onAttach(applicationContext)
        LocaleHelper.setLocale(sharedPrefs.getPreferredLocale())
    }

}