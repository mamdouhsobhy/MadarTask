package com.madar.madartask.madarTask

import android.os.Bundle
import com.madar.madartask.core.presentation.base.BaseActivity
import com.madar.madartask.core.presentation.dialog.CloseAppDialog
import com.madar.madartask.databinding.ActivityMainBinding
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : BaseActivity<ActivityMainBinding>() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }

    override fun onBackPressed() {
        CloseAppDialog {
            finish()
        }.show(supportFragmentManager, "")
    }
}