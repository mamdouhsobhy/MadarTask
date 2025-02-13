package com.madar.madartask.splash

import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import com.madar.madartask.madarTask.MainActivity
import com.madar.madartask.core.presentation.base.BaseActivity
import com.madar.madartask.core.presentation.common.SharedPrefs
import com.madar.madartask.databinding.ActivitySplashBinding
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class ActivitySplash : BaseActivity<ActivitySplashBinding>() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        animateUp(binding.imgSalah)
        Handler(Looper.getMainLooper()).postDelayed({

            val intent = Intent(this, MainActivity::class.java)
            startActivity(intent)
            finish()

        }, SPLASH_TIME_OUT)
    }

    companion object {
        private const val SPLASH_TIME_OUT: Long = 4000
    }

}