package com.devmasterteam.tasks.view

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.devmasterteam.tasks.databinding.FragmentAllTasksBinding
import com.devmasterteam.tasks.service.listener.TaskListener
import com.devmasterteam.tasks.view.adapter.TaskAdapter
import com.devmasterteam.tasks.viewmodel.TaskListViewModel

class AllTasksFragment : Fragment() {

    private lateinit var viewModel: TaskListViewModel
    private var _binding: FragmentAllTasksBinding? = null
    private var adapter: TaskAdapter = TaskAdapter()
    private val binding get() = _binding!!

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, b: Bundle?): View {
        viewModel = ViewModelProvider(this)[TaskListViewModel::class.java]
        _binding = FragmentAllTasksBinding.inflate(inflater, container, false)


        binding.recyclerAllTasks.layoutManager = LinearLayoutManager(context)
        binding.recyclerAllTasks.adapter = adapter

        val listener = object : TaskListener{
            override fun onListClick(id: Int) {
                val intent = Intent(context, TaskFormActivity::class.java)
                intent.putExtra("taskId", id)
                startActivity(intent)
            }

            override fun onDeleteClick(id: Int) {
                viewModel.delete(id)
            }

            override fun onCompleteClick(id: Int) {
                viewModel.completeTask(id)
            }

            override fun onUndoClick(id: Int) {
                viewModel.undoTask(id)
            }

        }

        adapter.attachListener(listener)

        viewModel.list()

        // Cria os observadores
        observe()

        return binding.root
    }

    override fun onResume() {
        super.onResume()
        viewModel.list()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    private fun observe() {
        viewModel.listData.observe(viewLifecycleOwner){
            adapter.updateTasks(it)
        }

        viewModel.removed.observe(viewLifecycleOwner){
            if(it){
                Toast.makeText(context, "Tarefa removida!", Toast.LENGTH_SHORT).show()
            }else{
                Toast.makeText(context, "Houve uma falha ao tentar remover a tarefa!", Toast.LENGTH_SHORT).show()
            }
        }

    }
}