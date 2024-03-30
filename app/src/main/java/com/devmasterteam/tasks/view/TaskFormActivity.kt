package com.devmasterteam.tasks.view

import android.app.DatePickerDialog
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.ArrayAdapter
import android.widget.DatePicker
import androidx.lifecycle.ViewModelProvider
import com.devmasterteam.tasks.databinding.ActivityTaskFormBinding
import com.devmasterteam.tasks.service.model.PriorityModel
import com.devmasterteam.tasks.service.model.TaskModel
import com.devmasterteam.tasks.viewmodel.TaskFormViewModel
import java.text.SimpleDateFormat
import java.util.Calendar

class TaskFormActivity : AppCompatActivity(), View.OnClickListener, DatePickerDialog.OnDateSetListener{

    private lateinit var viewModel: TaskFormViewModel
    private lateinit var binding: ActivityTaskFormBinding
    private var dateFormat: SimpleDateFormat = SimpleDateFormat("dd/MM/yyyy")
    private var listPriority: List<PriorityModel> = listOf()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)


        // Variáveis da classe
        viewModel = ViewModelProvider(this)[TaskFormViewModel::class.java]
        binding = ActivityTaskFormBinding.inflate(layoutInflater)

        // Eventos
        handleClicks()
        viewModel.listPriority()

        // Layout
        setContentView(binding.root)
        setSpinner()
    }

    override fun onClick(v: View) {
        when(v.id){
            binding.buttonSave.id -> save()
            binding.buttonDate.id -> handleDate()
        }
    }

    // trata evento apos user escolher a data, fixando a data escolhida para ele
    override fun onDateSet(date: DatePicker, year: Int, month: Int, day: Int) {
        val calendar = Calendar.getInstance()
        calendar.set(year, month, day)

        val dueDate = dateFormat.format(calendar.time)
        binding.buttonDate.text = dueDate
    }

    private fun handleClicks(){
        binding.buttonSave.setOnClickListener(this)
        binding.buttonDate.setOnClickListener(this)
    }

    private fun handleDate(){
        val calendar = Calendar.getInstance()
        val year = calendar.get(Calendar.YEAR)
        val month = calendar.get(Calendar.MONTH)
        val day = calendar.get(Calendar.DAY_OF_MONTH)

        DatePickerDialog(this, this, year, month, day).show()
    }

    private fun setSpinner(){
        viewModel.list.observe(this){
            val list = mutableListOf<String>()
            listPriority = it
            for(priority in it){
                list.add(priority.description)
            }
            val adapter = ArrayAdapter(this, android.R.layout.simple_spinner_dropdown_item, list)
            binding.spinnerPriority.adapter = adapter
        }
    }

    private fun save(){
        val task = TaskModel().apply {
            this.id = 0
            this.complete = binding.checkComplete.isChecked
            this.description = binding.editDescription.text.toString()
            this.priorityId = listPriority.get(binding.spinnerPriority.selectedItemPosition).id
            this.dueDate = binding.buttonDate.text.toString()
        }
        viewModel.save(task)
    }


}