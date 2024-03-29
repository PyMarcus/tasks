package com.devmasterteam.tasks.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.devmasterteam.tasks.service.listener.ApiListener
import com.devmasterteam.tasks.service.model.UserModel
import com.devmasterteam.tasks.service.repository.UserRepository

class LoginViewModel(application: Application) : AndroidViewModel(application) {

    private val userRepository: UserRepository = UserRepository()
    private var successLogin: MutableLiveData<UserModel> = MutableLiveData<UserModel>()
    private var _message: MutableLiveData<String> = MutableLiveData<String>()
    var login: LiveData<UserModel> = successLogin
    var message: LiveData<String> = _message

    fun doLogin(email: String, password: String) {
        userRepository.login(email, password, object : ApiListener<UserModel>{
            override fun onSuccess(response: UserModel) {
                successLogin.value = response
            }

            override fun onFail(message: String) {
                _message.value = message
            }

        })
    }


    fun verifyLoggedUser() {
    }

}