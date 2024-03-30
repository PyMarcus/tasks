package com.devmasterteam.tasks.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.devmasterteam.tasks.service.listener.ApiListener
import com.devmasterteam.tasks.service.model.TaskModel
import com.devmasterteam.tasks.service.repository.PriorityRepository
import com.devmasterteam.tasks.service.repository.TaskRepository

class TaskListViewModel (application: Application): AndroidViewModel(application) {
    private val remote = TaskRepository()
    private val priorityRepository = PriorityRepository(application.applicationContext)

    private var _listData: MutableLiveData<List<TaskModel>> = MutableLiveData()
    var listData: LiveData<List<TaskModel>> = _listData

    fun list(){
        remote.list(object : ApiListener<List<TaskModel>>{
            override fun onSuccess(response: List<TaskModel>) {
                response.forEach{
                    taskModel -> taskModel.priorityText = priorityRepository.getDescription(taskModel.priorityId)
                }
                _listData.value = response.sortedBy { it.dueDate }
            }
            override fun onFail(message: String) {
                println("Fail")
            }
        })
    }

}