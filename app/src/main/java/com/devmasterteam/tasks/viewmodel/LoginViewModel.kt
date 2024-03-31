package com.devmasterteam.tasks.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.devmasterteam.tasks.service.listener.ApiListener
import com.devmasterteam.tasks.service.model.PriorityModel
import com.devmasterteam.tasks.service.model.UserModel
import com.devmasterteam.tasks.service.model.ValidationModel
import com.devmasterteam.tasks.service.repository.BaseRepository
import com.devmasterteam.tasks.service.repository.PriorityRepository
import com.devmasterteam.tasks.service.repository.SecurityPreferences
import com.devmasterteam.tasks.service.repository.UserRepository
import com.devmasterteam.tasks.service.repository.remote.RetrofitClient

class LoginViewModel(application: Application) : AndroidViewModel(application) {
    private val securityPreferences: SecurityPreferences = SecurityPreferences(application.applicationContext)
    private val userRepository: UserRepository = UserRepository()
    private val priorityRepository: PriorityRepository = PriorityRepository(application.applicationContext)
    private var successLogin: MutableLiveData<ValidationModel> = MutableLiveData<ValidationModel>()
    private var isLogged: MutableLiveData<Boolean> = MutableLiveData()
    private var repo = BaseRepository(application.applicationContext)
    private var _listPriority: MutableLiveData<List<PriorityModel>> = MutableLiveData()
    var login: LiveData<ValidationModel> = successLogin
    var logged: LiveData<Boolean> = isLogged
    var listPriority: LiveData<List<PriorityModel>> = _listPriority

    fun doLogin(email: String, password: String) {
        userRepository.login(email, password, object : ApiListener<UserModel>{
            override fun onSuccess(response: UserModel) {
                securityPreferences.store("personKey", response.personKey)
                securityPreferences.store("token", response.token)
                securityPreferences.store("name", response.name)
                RetrofitClient.addHeaders(response.personKey, response.token)
                successLogin.value = ValidationModel()
            }

            override fun onFail(message: String) {
                var txt = message
                if(!repo.isConnectionAvaiable()){
                    txt = "Verifique as credenciais e a conexao e tente novamente!"
                }
                successLogin.value = ValidationModel(txt, false)
            }

        })
    }


    fun verifyLoggedUser() {
        val token = securityPreferences.get("token")
        val person = securityPreferences.get("personKey")

        RetrofitClient.addHeaders(person, token)
        val l: Boolean = token != "" && person != ""
        isLogged.value = l

        // baixa as prioridades, se nao estiver logado
        if(!l){
            priorityRepository.list(object : ApiListener<List<PriorityModel>>{
                override fun onSuccess(response: List<PriorityModel>) {
                    _listPriority.value = response
                    priorityRepository.save(response)
                }

                override fun onFail(message: String) {

                }

            })
        }
    }

}