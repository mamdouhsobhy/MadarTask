package com.madar.madartask.core.data.utils

import android.util.Log
import com.madar.madartask.core.presentation.base.BaseState
import kotlinx.coroutines.flow.*
import java.net.UnknownHostException
import javax.inject.Inject

class ExecuteResponse @Inject constructor() {

    fun <T : Any> execute(
        flow: Flow<BaseState<T>>,
        state: MutableStateFlow<BaseState<T>>
    ): Flow<BaseState<T>> {
        return flow
            .onStart { state.value = BaseState.IsLoading(true) }
            .catch { e ->
                Log.d("state",e.toString())
                state.value = BaseState.IsLoading(false)
                state.value = BaseState.ShowToast(
                    e.message.toString(),
                    e is UnknownHostException
                )
            }
            .map {
                Log.d("state",it.toString())
                state.value = BaseState.IsLoading(false)
                when (it) {
                    is BaseState.Error -> BaseState.Error(it.code, it.message)
                    is BaseState.Success -> BaseState.Success(it.items)
                    BaseState.Init -> TODO()
                    is BaseState.IsLoading -> TODO()
                    is BaseState.ShowToast -> TODO()
                }
            }
    }

}