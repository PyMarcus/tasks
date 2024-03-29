package com.devmasterteam.tasks.view

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.lifecycle.ViewModelProvider
import com.devmasterteam.tasks.databinding.ActivityLoginBinding
import com.devmasterteam.tasks.viewmodel.LoginViewModel

class LoginActivity : AppCompatActivity(), View.OnClickListener {

    private lateinit var viewModel: LoginViewModel
    private lateinit var binding: ActivityLoginBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // Variáveis da classe
        viewModel = ViewModelProvider(this)[LoginViewModel::class.java]
        binding = ActivityLoginBinding.inflate(layoutInflater)

        // Layout
        setContentView(binding.root)

        // Eventos
        binding.buttonLogin.setOnClickListener(this)
        binding.textRegister.setOnClickListener(this)

        viewModel.verifyLoggedUser()

        // Observadores
        observe()
    }

    override fun onClick(v: View) {
        when(v.id){
            binding.buttonLogin.id -> login()
            binding.textRegister.id -> register()
        }
    }

    private fun observe() {
        viewModel.login.observe(this) {
            if(it.status){
                startActivity(Intent(applicationContext, MainActivity::class.java))
            }else{
                Toast.makeText(applicationContext, it.message, Toast.LENGTH_LONG).show()
            }
        }

        viewModel.logged.observe(this){
            if(it){
                startActivity(Intent(applicationContext, MainActivity::class.java))
            }
        }
    }

    private fun login(){
        val email: String = binding.editEmail.text.toString()
        val password: String = binding.editPassword.text.toString()

        viewModel.doLogin(email, password)
    }

    private fun register(){
        startActivity(Intent(this, RegisterActivity::class.java))
    }
}