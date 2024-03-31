package com.devmasterteam.tasks.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.devmasterteam.tasks.service.listener.ApiListener
import com.devmasterteam.tasks.service.model.TaskModel
import com.devmasterteam.tasks.service.repository.PriorityRepository
import com.devmasterteam.tasks.service.repository.TaskRepository

class TaskListViewModel (application: Application): AndroidViewModel(application) {
    private val remote = TaskRepository()
    private val priorityRepository = PriorityRepository(application.applicationContext)

    private var _listData: MutableLiveData<List<TaskModel>> = MutableLiveData()
    var listData: LiveData<List<TaskModel>> = _listData

    private var _removed: MutableLiveData<Boolean> = MutableLiveData()
    var removed: LiveData<Boolean> = _removed

    fun completeTask(id: Int){
        remote.complete(id, object : ApiListener<Boolean>{
            override fun onSuccess(response: Boolean) {
                list()
            }

            override fun onFail(message: String) {
                println()
            }

        })
    }

    fun undoTask(id: Int){
        remote.undo(id, object : ApiListener<Boolean>{
            override fun onSuccess(response: Boolean) {
                println("OK, nao deu zebra ")
                list()
            }

            override fun onFail(message: String) {
                println("OK, deu zebra $message")
            }

        })
    }

    fun list(){
        remote.list(object : ApiListener<List<TaskModel>>{
            override fun onSuccess(response: List<TaskModel>) {
                response.forEach{
                    taskModel -> taskModel.priorityText = priorityRepository.getDescription(taskModel.priorityId)
                }
                _listData.value = response
            }
            override fun onFail(message: String) {
                println("Fail")
            }
        })
    }

    fun delete(id: Int){
        remote.delete(id, object : ApiListener<Boolean>{
            override fun onSuccess(response: Boolean) {
                list()
                _removed.value = true
            }

            override fun onFail(message: String) {
                _removed.value = false
            }

        })
    }

}