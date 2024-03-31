package com.devmasterteam.tasks.viewmodel

import android.app.Application
import android.os.Build
import androidx.annotation.RequiresApi
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.devmasterteam.tasks.service.listener.ApiListener
import com.devmasterteam.tasks.service.model.TaskModel
import com.devmasterteam.tasks.service.repository.PriorityRepository
import com.devmasterteam.tasks.service.repository.TaskRepository

class TaskListViewModel (application: Application): AndroidViewModel(application) {
    private val remote = TaskRepository(application)
    private val priorityRepository = PriorityRepository(application.applicationContext)

    private var _listData: MutableLiveData<List<TaskModel>> = MutableLiveData()
    var listData: LiveData<List<TaskModel>> = _listData

    private var _msg: MutableLiveData<String> = MutableLiveData()
    var msg: LiveData<String> = _msg

    private var _removed: MutableLiveData<Boolean> = MutableLiveData()
    var removed: LiveData<Boolean> = _removed

    private var taskF = 0

    fun completeTask(id: Int){
        remote.complete(id, object : ApiListener<Boolean>{
            override fun onSuccess(response: Boolean) {
                list(taskF)
            }

            override fun onFail(message: String) {
                _msg.value = message
                _listData.value = listOf()
            }

        })
    }

    fun undoTask(id: Int){
        remote.undo(id, object : ApiListener<Boolean>{
            override fun onSuccess(response: Boolean) {
                list(taskF)
            }

            override fun onFail(message: String) {
                _msg.value = message
                _listData.value = listOf()
            }

        })
    }

    fun list(taskFilter: Int){
        taskF = taskFilter
        val listener = object : ApiListener<List<TaskModel>>{
            override fun onSuccess(response: List<TaskModel>) {
                response.forEach{
                        taskModel -> taskModel.priorityText = priorityRepository.getDescription(taskModel.priorityId)
                }
                _listData.value = response
            }
            override fun onFail(message: String) {
                _msg.value = message
                _listData.value = listOf()
            }
        }

        // all
        when (taskFilter) {
            0 -> {
                remote.list(listener)
            }
            // next
            1 -> {
                remote.listNext7(listener)
            }
            // overdue
            2 -> {
                remote.listOverdue(listener)
            }
        }

    }

    fun delete(id: Int){
        remote.delete(id, object : ApiListener<Boolean>{
            override fun onSuccess(response: Boolean) {
                list(0)
                _removed.value = true
            }

            override fun onFail(message: String) {
                _removed.value = false
                _msg.value = message
                _listData.value = listOf()
            }

        })
    }

}