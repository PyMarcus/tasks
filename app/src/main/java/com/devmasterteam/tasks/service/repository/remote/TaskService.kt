package com.devmasterteam.tasks.service.repository.remote

import com.devmasterteam.tasks.service.model.TaskModel
import retrofit2.Call
import retrofit2.http.DELETE
import retrofit2.http.Field
import retrofit2.http.FormUrlEncoded
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.PUT
import retrofit2.http.Path

interface TaskService {

    @POST("Task")
    @FormUrlEncoded
    fun create(
        @Field("PriorityId") priorityId: Int,
        @Field("Description") description: String,
        @Field("DueDate") dueDate: String,
        @Field("Complete") complete: String
    ): Call<Boolean>

    @PUT("Task")
    @FormUrlEncoded
    fun update(
        @Field("Id") id: Int,
        @Field("PriorityId") priorityId: Int,
        @Field("Description") description: String,
        @Field("DueDate") dueDate: String,
        @Field("Complete") complete: String
    ): Call<Boolean>

    @PUT("Task/Undo")
    @FormUrlEncoded
    fun updateUncomplete(
        @Field("Id") id: Int,
    ): Call<Boolean>

    @PUT("Task/Complete")
    @FormUrlEncoded
    fun updateComplete(
        @Field("Id") id: Int,
    ): Call<Boolean>

    @DELETE("Task")
    @FormUrlEncoded
    fun delete(
        @Field("Id") id: Int,
    ): Call<Boolean>

    @GET("Task")
    fun list() : Call<List<TaskModel>>

    @GET("Task/Next7Days")
    fun listNext7(): Call<List<TaskModel>>

    @GET("Task/Overdue")
    fun listOverdue(): Call<List<TaskModel>>

    @GET("Task/{id}")
    fun getTask(@Path(value="id", encoded = true) id: Int): Call<TaskModel>
}