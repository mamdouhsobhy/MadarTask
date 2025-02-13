package com.madar.madartask.core.data.utils

import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import com.madar.madartask.core.presentation.base.BaseState
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import retrofit2.Response
import javax.inject.Inject

class EmittingResponse @Inject constructor() {

    suspend fun <T : Any> makeApiCall(call: suspend () -> Response<T>): Flow<BaseState<T>> {
        return flow {
            val response = call.invoke()

            if (response.isSuccessful) {
                val body = response.body()!!
                if(response.code().toString() == "401"){
                    emit(BaseState.Error("400", "invalid email or password"))
                }else {
                    emit(BaseState.Success(body))
                }
            } else {
                val type = object : TypeToken<T>() {}.type
                val err: T =
                    Gson().fromJson(response.errorBody()!!.charStream(), type)
                if(response.code() == 401){
                    emit(BaseState.Error("401", "Un authenticated"))
                }else {
                    emit(
                        BaseState.Error(
                            "ResponseCodeError",
                            "ResponseMessageError"
                        )
                    )
                }

            }
        }
    }
}
