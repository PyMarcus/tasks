package com.devmasterteam.tasks.service.listener

interface ApiListener<T> {
    fun onSuccess(response: T)

    fun onFail(message: String)
}