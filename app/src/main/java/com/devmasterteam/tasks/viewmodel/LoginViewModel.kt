package com.devmasterteam.tasks.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.devmasterteam.tasks.service.listener.ApiListener
import com.devmasterteam.tasks.service.model.UserModel
import com.devmasterteam.tasks.service.model.ValidationModel
import com.devmasterteam.tasks.service.repository.UserRepository

class LoginViewModel(application: Application) : AndroidViewModel(application) {

    private val userRepository: UserRepository = UserRepository()
    private var successLogin: MutableLiveData<ValidationModel> = MutableLiveData<ValidationModel>()
    var login: LiveData<ValidationModel> = successLogin

    fun doLogin(email: String, password: String) {
        userRepository.login(email, password, object : ApiListener<UserModel>{
            override fun onSuccess(response: UserModel) {
                successLogin.value = ValidationModel()
            }

            override fun onFail(message: String) {
                successLogin.value = ValidationModel(message, false)
            }

        })
    }


    fun verifyLoggedUser() {
    }

}