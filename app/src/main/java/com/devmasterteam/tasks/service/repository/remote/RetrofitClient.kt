package com.devmasterteam.tasks.service.repository.remote

import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.create

class RetrofitClient {
    companion object{
        private lateinit var INSTANCE: Retrofit
        private const val URL_BASE: String = "http://devmasterteam.com/CursoAndroidAPI/"


        private fun getRetrofitInstance(): Retrofit{
            if(!::INSTANCE.isInitialized){
                // a fim de evitar problemas com multithread.
                synchronized(RetrofitClient::class.java){
                    INSTANCE = Retrofit.Builder()
                        .baseUrl(URL_BASE)
                        .client(OkHttpClient.Builder().build())
                        .addConverterFactory(GsonConverterFactory.create())
                        .build()
                }
            }
            return INSTANCE
        }

        // servico generico
        fun <T> getService(_class: Class<T>) : T{
            return getRetrofitInstance().create(_class)
        }
    }
}