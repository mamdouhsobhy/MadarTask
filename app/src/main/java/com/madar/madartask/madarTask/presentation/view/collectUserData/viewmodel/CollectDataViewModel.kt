package com.madar.madartask.madarTask.presentation.view.collectUserData.viewmodel

import androidx.lifecycle.ViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import com.madar.madartask.core.presentation.common.SharedPrefs
import com.madar.madartask.madarTask.data.ModelUserData
import kotlinx.coroutines.flow.*
import javax.inject.Inject

@HiltViewModel
class CollectDataViewModel @Inject constructor(
    private val sharedPrefs: SharedPrefs
) : ViewModel() {

    private val _collectUserDataState =
        MutableStateFlow<ModelUserData?>(sharedPrefs.getUser())
    val collectUserDataState: StateFlow<ModelUserData?> get() = _collectUserDataState

    fun collectUserData(modelUserData: ModelUserData?) {
        if (modelUserData != null) {
            sharedPrefs.saveUser(modelUserData)
            _collectUserDataState.value = modelUserData
        }
    }

}
