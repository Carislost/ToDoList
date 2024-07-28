package udb.edu.sv

import android.app.Application
import android.content.Context
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken

class TaskViewModel(application: Application) : AndroidViewModel(application) {

    private val _tasks = MutableLiveData<MutableList<Task>>(mutableListOf())
    val tasks: LiveData<MutableList<Task>> get() = _tasks

    private val sharedPreferences = application.getSharedPreferences("tasks", Context.MODE_PRIVATE)
    private val gson = Gson()

    init {
        loadTasks()
    }

    fun addTask(task: Task) {
        _tasks.value?.let {
            it.add(task)
            _tasks.value = it
            saveTasks()
        }
    }

    fun updateTask(task: Task) {
        _tasks.value?.let {
            val index = it.indexOfFirst { t -> t.id == task.id }
            if (index != -1) {
                it[index] = task
                _tasks.value = it
                saveTasks()
            }
        }
    }

    fun deleteTask(task: Task) {
        _tasks.value?.let {
            it.remove(task)
            _tasks.value = it
            saveTasks()
        }
    }

    private fun saveTasks() {
        val tasksJson = gson.toJson(_tasks.value)
        sharedPreferences.edit().putString("tasks", tasksJson).apply()
    }

    private fun loadTasks() {
        val tasksJson = sharedPreferences.getString("tasks", null)
        if (tasksJson != null) {
            val type = object : TypeToken<MutableList<Task>>() {}.type
            val tasks: MutableList<Task> = gson.fromJson(tasksJson, type)
            _tasks.value = tasks
        }
    }
}
