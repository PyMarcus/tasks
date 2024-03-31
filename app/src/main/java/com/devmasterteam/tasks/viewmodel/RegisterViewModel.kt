package com.devmasterteam.tasks.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.devmasterteam.tasks.service.listener.ApiListener
import com.devmasterteam.tasks.service.repository.UserRepository
import com.devmasterteam.tasks.service.repository.remote.TaskService
import com.devmasterteam.tasks.service.repository.remote.UserService

class RegisterViewModel(application: Application) : AndroidViewModel(application) {

    private val repository = UserRepository()
    private var _register = MutableLiveData<Boolean>()
    var register: LiveData<Boolean> = _register

    fun create(name: String, email: String, password: String) {
        var call = repository.create(name, email, password,object :ApiListener<Boolean>{
            override fun onSuccess(response: Boolean) {
                _register.value = response
            }

            override fun onFail(message: String) {
                _register.value = false
            }

        })
    }

}