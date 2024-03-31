package com.devmasterteam.tasks.service.repository

import android.content.Context
import android.net.ConnectivityManager
import android.net.NetworkCapabilities
import android.os.Build
import androidx.annotation.RequiresApi
import com.devmasterteam.tasks.service.constants.TaskConstants
import com.devmasterteam.tasks.service.listener.ApiListener
import com.google.gson.Gson

open class BaseRepository (val context: Context){
    open fun failResponse(content: String): String{
        return Gson().fromJson(content, String::class.java)
    }

    open fun <T> handleResponse(r: Int, ok: Int, result: T, listener: ApiListener<T>){
        if(r == ok){
            println("FALHAJ")
            listener.onSuccess(result)
        }else{
            listener.onFail("Falha ao criar tarefa!")
        }
    }


    /*
    * Verifica se esta conectado ao wifi ou rede movel
    * */
    open fun isConnectionAvaiable(): Boolean {
        val cm = context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
        val an = cm.activeNetwork
        val gnc = cm.getNetworkCapabilities(an)

        return if (gnc != null) {
            gnc.hasTransport(NetworkCapabilities.TRANSPORT_CELLULAR) || gnc.hasTransport(NetworkCapabilities.TRANSPORT_WIFI)
        } else {
            false
        }
    }
}