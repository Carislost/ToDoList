package udb.edu.sv

import android.graphics.Paint
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.CheckBox
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView

class TaskAdapter(
    private val tasks: MutableList<Task>,
    private val onTaskCheckedChange: (Task) -> Unit,
    private val onTaskDelete: (Task) -> Unit
) : RecyclerView.Adapter<TaskAdapter.TaskViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TaskViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_task, parent, false)
        return TaskViewHolder(view)
    }

    override fun onBindViewHolder(holder: TaskViewHolder, position: Int) {
        holder.bind(tasks[position])
    }

    override fun getItemCount(): Int = tasks.size

    fun updateTasks(newTasks: List<Task>) {
        tasks.clear()
        tasks.addAll(newTasks)
        notifyDataSetChanged()
    }

    inner class TaskViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private val taskName: TextView = itemView.findViewById(R.id.taskName)
        private val taskCompleted: CheckBox = itemView.findViewById(R.id.taskCompleted)
        private val deleteButton: Button = itemView.findViewById(R.id.deleteButton)

        fun bind(task: Task) {
            taskName.text = task.name
            taskCompleted.isChecked = task.isCompleted
            taskName.paintFlags = if (task.isCompleted) {
                taskName.paintFlags or Paint.STRIKE_THRU_TEXT_FLAG
            } else {
                taskName.paintFlags and Paint.STRIKE_THRU_TEXT_FLAG.inv()
            }

            // Clear previous listeners to avoid multiple calls
            taskCompleted.setOnCheckedChangeListener(null)
            taskCompleted.setOnCheckedChangeListener { _, isChecked ->
                task.isCompleted = isChecked
                onTaskCheckedChange(task)
                notifyItemChanged(bindingAdapterPosition)
            }

            deleteButton.setOnClickListener {
                val position = bindingAdapterPosition
                if (position != RecyclerView.NO_POSITION) {
                    onTaskDelete(task)
                }
            }
        }
    }

    fun removeTask(task: Task) {
        val position = tasks.indexOf(task)
        if (position != -1) {
            tasks.removeAt(position)
            notifyItemRemoved(position)
            notifyItemRangeChanged(position, tasks.size)
        }
    }
}
