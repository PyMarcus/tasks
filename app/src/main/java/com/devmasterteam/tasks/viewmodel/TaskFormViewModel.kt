package com.devmasterteam.tasks.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.devmasterteam.tasks.service.listener.ApiListener
import com.devmasterteam.tasks.service.model.PriorityModel
import com.devmasterteam.tasks.service.model.TaskModel
import com.devmasterteam.tasks.service.repository.PriorityRepository
import com.devmasterteam.tasks.service.repository.TaskRepository
import com.devmasterteam.tasks.service.repository.local.TaskDatabase

class TaskFormViewModel(application: Application) : AndroidViewModel(application) {
    private val repository = PriorityRepository(application.applicationContext)
    private val remote = TaskRepository()
    private var _list: MutableLiveData<List<PriorityModel>> = MutableLiveData()
    var list: LiveData<List<PriorityModel>> = _list

    fun listPriority(){
        _list.value = repository.list()
    }

    fun save(task: TaskModel){
        remote.create(task, object: ApiListener<Boolean>{
            override fun onSuccess(response: Boolean) {
                println()
            }

            override fun onFail(message: String) {
                println()
            }

        })
    }
}