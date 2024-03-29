package com.devmasterteam.tasks.service.repository.remote

import okhttp3.Interceptor
import okhttp3.OkHttpClient
import okhttp3.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.create

class RetrofitClient {
    companion object{
        private lateinit var INSTANCE: Retrofit
        private const val URL_BASE: String = "http://devmasterteam.com/CursoAndroidAPI/"

        private var TOKEN = ""
        private var PERSONKEY = ""

        private fun getRetrofitInstance(): Retrofit{
            val http = OkHttpClient.Builder()
            // add headers a requisicao
            http.addInterceptor(object : Interceptor{
                override fun intercept(chain: Interceptor.Chain): Response {
                    val request = chain.request()
                        .newBuilder()
                        .addHeader("token", TOKEN)
                        .addHeader("personKey", PERSONKEY)
                        .build()
                    return chain.proceed(request)
                }

            })

            if(!::INSTANCE.isInitialized){
                // a fim de evitar problemas com multithread.
                synchronized(RetrofitClient::class.java){
                    INSTANCE = Retrofit.Builder()
                        .baseUrl(URL_BASE)
                        .client(http.build())
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

        fun addHeaders(personKey: String, token: String){
            TOKEN = token
            PERSONKEY = personKey
        }
    }
}