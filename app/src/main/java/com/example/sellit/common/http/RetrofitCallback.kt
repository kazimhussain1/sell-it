package com.example.sellit.common.http

import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class RetrofitCallback<T>(val resultCallback: (result:T)-> Unit, val errorCallback: (error:String)->Unit) : Callback<T> {
    override fun onResponse(call: Call<T>, response: Response<T>) {
        if (response.isSuccessful && response.body() != null)
            resultCallback(response.body()!!)
        else
            errorCallback(NetworkUtility.extractErrorBody(response.errorBody()))
    }

    override fun onFailure(call: Call<T>, t: Throwable) {
        errorCallback(NetworkUtility.generateError(t))
    }
}
