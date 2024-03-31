package com.devmasterteam.tasks.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import com.devmasterteam.tasks.service.repository.SecurityPreferences

class MainViewModel(application: Application): AndroidViewModel(application) {

    private val securityPreferences = SecurityPreferences(application.applicationContext)


    fun logout(){
        securityPreferences.remove("token")
        securityPreferences.remove("personKey")
        securityPreferences.remove("name")
    }
}