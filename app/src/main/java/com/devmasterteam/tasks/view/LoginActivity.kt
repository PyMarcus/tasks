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
            startActivity(Intent(applicationContext, MainActivity::class.java))
        }

        viewModel.message.observe(this){
            Toast.makeText(applicationContext, it, Toast.LENGTH_LONG).show()
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