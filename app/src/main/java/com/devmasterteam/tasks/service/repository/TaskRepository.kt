package com.devmasterteam.tasks.service.repository

import com.devmasterteam.tasks.service.constants.TaskConstants
import com.devmasterteam.tasks.service.listener.ApiListener
import com.devmasterteam.tasks.service.model.TaskModel
import com.devmasterteam.tasks.service.model.UserModel
import com.devmasterteam.tasks.service.repository.remote.RetrofitClient
import com.devmasterteam.tasks.service.repository.remote.TaskService
import com.devmasterteam.tasks.service.repository.remote.UserService
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class TaskRepository : BaseRepository(){
    private val service = RetrofitClient.getService(TaskService::class.java)

    fun list(apiListener: ApiListener<List<TaskModel>>){
        val call = service.list()
        listData(call, apiListener)
    }

    fun listNext7(apiListener: ApiListener<List<TaskModel>>){
        val call = service.listNext7()
        listData(call, apiListener)
    }

    fun listOverdue(apiListener: ApiListener<List<TaskModel>>){
        val call = service.listOverdue()
        listData(call, apiListener)
    }


    fun create(task: TaskModel, listener: ApiListener<Boolean>){
        val call = service.create(task.priorityId, task.description, task.dueDate, task.complete)
        call.enqueue(object : Callback<Boolean>{
            override fun onResponse(p0: Call<Boolean>, r: Response<Boolean>) {
                handleResponse(r.code(), TaskConstants.STATUS.OK, true, listener)
            }

            override fun onFailure(p0: Call<Boolean>, p1: Throwable) {
                listener.onFail("Erro ao criar tarefa! Por favor, verifique os campos.")
            }

        })
    }

    private fun listData(call: Call<List<TaskModel>>, apiListener: ApiListener<List<TaskModel>>){
        call.enqueue(object : Callback<List<TaskModel>> {
            override fun onResponse(p0: Call<List<TaskModel>>, r: Response<List<TaskModel>>) {
                if(r.code() == TaskConstants.STATUS.OK){
                    r.body()?.let { apiListener.onSuccess(it) }
                }else{
                    apiListener.onFail(TaskConstants.MESSAGES.BAD_REQUEST)
                }
            }

            override fun onFailure(p0: Call<List<TaskModel>>, t: Throwable) {
                apiListener.onFail(t.toString())
            }
        })
    }
}