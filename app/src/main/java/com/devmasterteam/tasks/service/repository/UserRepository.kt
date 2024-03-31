package com.devmasterteam.tasks.service.repository

import com.devmasterteam.tasks.service.constants.TaskConstants.MESSAGES.BAD_REQUEST
import com.devmasterteam.tasks.service.constants.TaskConstants.MESSAGES.FAIL
import com.devmasterteam.tasks.service.constants.TaskConstants.STATUS.OK
import com.devmasterteam.tasks.service.listener.ApiListener
import com.devmasterteam.tasks.service.model.UserModel
import com.devmasterteam.tasks.service.repository.remote.RetrofitClient
import com.devmasterteam.tasks.service.repository.remote.UserService
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class UserRepository {

    private val service = RetrofitClient.getService(UserService::class.java)

    fun login(email: String, password: String, apiListener: ApiListener<UserModel>){
        val call = service.login(email, password)
        call.enqueue(object : Callback<UserModel> {
            override fun onResponse(p0: Call<UserModel>, r: Response<UserModel>) {
                if(r.code() == OK){
                    r.body()?.let { apiListener.onSuccess(it) }
                }else{
                    apiListener.onFail(BAD_REQUEST)
                }
            }

            override fun onFailure(p0: Call<UserModel>, t: Throwable) {
                apiListener.onFail(t.toString())
            }
        })
    }

    fun create(name: String, email: String, password: String, apiListener: ApiListener<Boolean>){
        val call = service.create(name, email, password)
        call.enqueue(object : Callback<UserModel> {
            override fun onResponse(p0: Call<UserModel>, r: Response<UserModel>) {
                if(r.code() == OK){
                    r.body()?.let { apiListener.onSuccess(true) }
                }else{
                    apiListener.onFail(BAD_REQUEST)
                }
            }

            override fun onFailure(p0: Call<UserModel>, t: Throwable) {
                apiListener.onFail(t.toString())
            }
        })
    }
}