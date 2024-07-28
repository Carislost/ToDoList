package udb.edu.sv

import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView

class MainActivity : AppCompatActivity() {

    private lateinit var taskViewModel: TaskViewModel
    private lateinit var taskAdapter: TaskAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        taskViewModel = ViewModelProvider(this).get(TaskViewModel::class.java)

        val recyclerView: RecyclerView = findViewById(R.id.recyclerView)
        val taskInput: EditText = findViewById(R.id.taskInput)
        val addButton: Button = findViewById(R.id.addButton)

        taskAdapter = TaskAdapter(mutableListOf(), { task ->
            taskViewModel.updateTask(task)
        }, { task ->
            taskViewModel.deleteTask(task)
            taskAdapter.removeTask(task)
        })

        recyclerView.layoutManager = LinearLayoutManager(this)
        recyclerView.adapter = taskAdapter

        taskViewModel.tasks.observe(this, { tasks ->
            taskAdapter.updateTasks(tasks)
        })

        addButton.setOnClickListener {
            val taskName = taskInput.text.toString()
            if (taskName.isNotEmpty()) {
                val newTask = Task(id = taskAdapter.itemCount, name = taskName, isCompleted = false)
                taskViewModel.addTask(newTask)
                taskInput.text.clear()
            }
        }
    }
}
