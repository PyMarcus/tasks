package com.devmasterteam.tasks.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.devmasterteam.tasks.service.repository.SecurityPreferences

class MainViewModel(application: Application): AndroidViewModel(application) {

    private val securityPreferences = SecurityPreferences(application.applicationContext)
    private var _name = MutableLiveData<String>()
    var name: LiveData<String> = _name

    fun logout(){
        securityPreferences.remove("token")
        securityPreferences.remove("personKey")
        securityPreferences.remove("name")
    }

    fun loadUserName(){
        _name.value = securityPreferences.get("name")
    }
}