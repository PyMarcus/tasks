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

class TaskFormViewModel(application: Application) : AndroidViewModel(application) {
    //private val securityPreferences: SecurityPreferences = SecurityPreferences(application.applicationContext)
    private val repository = PriorityRepository(application.applicationContext)
    private val remote = TaskRepository()

    private var _list: MutableLiveData<List<PriorityModel>> = MutableLiveData()
    var list: LiveData<List<PriorityModel>> = _list

    private var _save: MutableLiveData<Boolean> = MutableLiveData()
    var save: LiveData<Boolean> = _save

    private var _update: MutableLiveData<Boolean> = MutableLiveData()
    var update: LiveData<Boolean> = _update

    private var _task = MutableLiveData<TaskModel>()
    var task: LiveData<TaskModel> = _task

    private var _failTask = MutableLiveData<String>()
    var failTask: LiveData<String> = _failTask

    fun listPriority(){
        _list.value = repository.list()
    }

    fun save(task: TaskModel){
        if(task.id == 0){
            remote.create(task, object: ApiListener<Boolean>{
                override fun onSuccess(response: Boolean) {
                    _save.value = true
                }

                override fun onFail(message: String) {
                    _save.value = false
                }

            })
        }else {
            remote.update(task, object : ApiListener<Boolean> {
                override fun onSuccess(response: Boolean) {
                    _update.value = true
                }

                override fun onFail(message: String) {
                    _update.value = false
                }

            })
        }
    }

    fun load(id: Int){
        remote.load(id, object: ApiListener<TaskModel>{
            override fun onSuccess(response: TaskModel) {
                _task.value = response
            }

            override fun onFail(message: String) {
                _failTask.value = "Falha ao carregar informações!"
            }

        })
    }
}