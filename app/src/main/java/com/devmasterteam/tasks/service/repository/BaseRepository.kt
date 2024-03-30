package com.devmasterteam.tasks.service.repository

import com.devmasterteam.tasks.service.constants.TaskConstants
import com.devmasterteam.tasks.service.listener.ApiListener
import com.google.gson.Gson

open class BaseRepository {
    open fun failResponse(content: String): String{
        return Gson().fromJson(content, String::class.java)
    }

    open fun <T, S> handleResponse(r: Int, ok: Int, result: T, error: S, listener: ApiListener<T>){
        if(r == ok){
            listener.onSuccess(result)
        }else{
            listener.onFail(failResponse(error.toString()))
        }
    }
}