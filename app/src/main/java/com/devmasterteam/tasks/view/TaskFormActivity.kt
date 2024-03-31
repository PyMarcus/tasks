package com.devmasterteam.tasks.view

import android.app.DatePickerDialog
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.ArrayAdapter
import android.widget.DatePicker
import android.widget.Toast
import androidx.lifecycle.ViewModelProvider
import com.devmasterteam.tasks.databinding.ActivityTaskFormBinding
import com.devmasterteam.tasks.service.model.PriorityModel
import com.devmasterteam.tasks.service.model.TaskModel
import com.devmasterteam.tasks.viewmodel.TaskFormViewModel
import java.text.SimpleDateFormat
import java.util.Calendar

class  TaskFormActivity : AppCompatActivity(), View.OnClickListener, DatePickerDialog.OnDateSetListener{

    private lateinit var viewModel: TaskFormViewModel
    private lateinit var binding: ActivityTaskFormBinding
    private var dateFormat: SimpleDateFormat = SimpleDateFormat("dd/MM/yyyy")
    private var listPriority: List<PriorityModel> = listOf()
    private var _taskId: Int = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)


        // Variáveis da classe
        viewModel = ViewModelProvider(this)[TaskFormViewModel::class.java]
        binding = ActivityTaskFormBinding.inflate(layoutInflater)


        // Eventos
        handleClicks()
        viewModel.listPriority()

        loadDataFromActivity()


        // Layout
        setContentView(binding.root)
        setSpinner()

        // Observe
        observe()
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

    private fun loadDataFromActivity(){
            val taskId = intent.getIntExtra("taskId", 0)
            _taskId = taskId
            if(_taskId != 0)
            viewModel.load(taskId)
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
            this.id = _taskId
            this.complete = binding.checkComplete.isChecked
            this.description = binding.editDescription.text.toString()
            this.priorityId = listPriority[binding.spinnerPriority.selectedItemPosition].id
            this.dueDate = binding.buttonDate.text.toString()
        }
        viewModel.save(task)
    }

    private fun observe(){
        viewModel.save.observe(this){
            if(it){
                createToast("Tarefa criada!")
                finish()
            }else{
                createToast("Erro ao criar tarefa! Por favor, verifique os campos.")
            }
        }

        viewModel.task.observe(this){
            binding.editDescription.setText(it.description)
            val date = SimpleDateFormat("yyyy-MM-dd").parse(it.dueDate)
            binding.buttonDate.text = SimpleDateFormat("dd/MM/yyyy").format(date)
            binding.spinnerPriority.setSelection(getIndex(it.priorityId))
            binding.checkComplete.isChecked = it.complete
        }

        viewModel.failTask.observe(this){
            if(it != null){
                Toast.makeText(this, it, Toast.LENGTH_SHORT).show()
                finish()
            }
        }

        viewModel.update.observe(this){
            if(it){
                Toast.makeText(this, "Atualizado!", Toast.LENGTH_SHORT).show()
                finish()
            }else{
                Toast.makeText(this, "Falha ao atualizar", Toast.LENGTH_SHORT).show()
                finish()
            }
        }
    }

    private fun getIndex(id: Int) : Int{
        var index = 0
        for(l in listPriority){
            if(l.id == id){
                break
            }
            println("ITEM ${l.id} ${l.description} ${id}")
            index++
        }
        if(index == listPriority.size) index --
        return index
    }

    // cria mensagem de aviso e fecha a activity
    private fun createToast(message: String){
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show()
    }

}