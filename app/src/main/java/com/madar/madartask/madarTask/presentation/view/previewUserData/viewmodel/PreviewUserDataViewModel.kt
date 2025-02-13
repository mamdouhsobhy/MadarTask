package com.madar.madartask.madarTask.presentation.view.previewUserData.viewmodel

import androidx.lifecycle.ViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import com.madar.madartask.core.presentation.common.SharedPrefs
import com.madar.madartask.core.presentation.utilities.LocaleHelper
import com.madar.madartask.core.presentation.utilities.Nav
import com.madar.madartask.madarTask.data.ModelUserData
import kotlinx.coroutines.flow.*
import javax.inject.Inject

@HiltViewModel
class PreviewUserDataViewModel @Inject constructor(
    private val sharedPrefs: SharedPrefs,
) : ViewModel() {

    private val _getUserDataState =
        MutableStateFlow<ModelUserData?>(sharedPrefs.getUser())
    val getUserDataState: StateFlow<ModelUserData?> get() = _getUserDataState

    private val _deleteUserDataState =
        MutableStateFlow<ModelUserData?>(sharedPrefs.getUser())
    val deleteUserDataState: StateFlow<ModelUserData?> get() = _deleteUserDataState

    fun updateUserData(modelUserData: ModelUserData?) {
         sharedPrefs.saveUser(modelUserData)
        _getUserDataState.value = modelUserData
    }

    fun deleteData() {
        sharedPrefs.saveUser(null)
        _deleteUserDataState.value = null
    }

    fun changeLanguage(lang:String){
        sharedPrefs.setPreferredLocale(lang)
        LocaleHelper.setLocale(lang)
    }
}
