package com.devmasterteam.tasks.service.repository

import android.content.Context
import com.devmasterteam.tasks.service.constants.TaskConstants
import com.devmasterteam.tasks.service.listener.ApiListener
import com.devmasterteam.tasks.service.model.PriorityModel
import com.devmasterteam.tasks.service.repository.local.PriorityDAO
import com.devmasterteam.tasks.service.repository.local.TaskDatabase
import com.devmasterteam.tasks.service.repository.remote.PriorityService
import com.devmasterteam.tasks.service.repository.remote.RetrofitClient
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class PriorityRepository(context: Context) {

    private val database: PriorityDAO = TaskDatabase.getDatabase(context).priorityDao()
    private val service = RetrofitClient.getService(PriorityService::class.java)

    fun save(list: List<PriorityModel>){
        database.clear()
        database.save(list)
    }
    fun list(apiListener: ApiListener<List<PriorityModel>>){
        val call = service.list()
        call.enqueue(object : Callback<List<PriorityModel>> {
            override fun onResponse(p0: Call<List<PriorityModel>>, r: Response<List<PriorityModel>>) {
                if(r.code() == TaskConstants.STATUS.OK){
                    apiListener.onSuccess(r.body()!!.toList())
                }else{
                    apiListener.onFail(TaskConstants.MESSAGES.BAD_REQUEST)
                }
            }

            override fun onFailure(p0: Call<List<PriorityModel>>, t: Throwable) {
                apiListener.onFail(t.toString())
            }
        })
    }
}

