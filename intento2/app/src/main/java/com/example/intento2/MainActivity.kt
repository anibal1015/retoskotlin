import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp

data class Task(val id: Long, val name: String, var isCompleted: Boolean) {

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            TaskApp()
        }
    }
}

@Composable
fun TaskApp() {
    val tasks = remember { mutableStateListOf<Task>() }
    val newTask = remember { mutableStateOf("") }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text(text = "Lista de Tareas") },
                elevation = 4.dp
            )
        },
        content = {
            Column(modifier = Modifier.padding(16.dp)) {
                TaskInput(
                    taskName = newTask.value,
                    onTaskNameChange = { newTask.value = it },
                    onAddTask = {
                        tasks.add(Task(System.currentTimeMillis(), it, false))
                        newTask.value = ""
                    }
                )
                Spacer(modifier = Modifier.height(16.dp))
                TaskList(tasks = tasks, onToggleTask = { task ->
                    task.isCompleted = !task.isCompleted
                })
            }
        },
        floatingActionButton = {
            DeleteCompletedButton(onDeleteCompleted = {
                tasks.removeAll { it.isCompleted }
            })
        }
    )
}

@Composable
fun TaskInput(taskName: String, onTaskNameChange: (String) -> Unit, onAddTask: () -> Unit) {
    Row(modifier = Modifier.fillMaxWidth()) {
        TextField(
            modifier = Modifier.weight(1f),
            value = taskName,
            onValueChange = { onTaskNameChange(it) },
            label = { Text("Nueva Tarea") },
            singleLine = true
        )
        Button(
            onClick = { onAddTask() },
            enabled = taskName.isNotBlank(),
            modifier = Modifier.padding(start = 8.dp)
        ) {
            Text(text = "Agregar")
        }
    }
}

@Composable
fun TaskList(tasks: List<Task>, onToggleTask: (Task) -> Unit) {
    LazyColumn(modifier = Modifier.fillMaxSize()) {
        items(tasks) { task ->
            TaskItem(task = task, onToggleTask = onToggleTask)
        }
    }
}

@Composable
fun TaskItem(task: Task, onToggleTask: (Task) -> Unit) {
    Row(verticalAlignment = Alignment.CenterVertically) {
        Checkbox(
            checked = task.isCompleted,
            onCheckedChange = { onToggleTask(task) },
            modifier = Modifier.padding(end = 16.dp)
        )
        Text(
            text = task.name,
            style = MaterialTheme.typography.body1,
            modifier = Modifier.weight(1f)
        )
    }
}

@Composable
fun DeleteCompletedButton(onDeleteCompleted: () -> Unit) {
    Button(
        onClick = onDeleteCompleted,
        modifier = Modifier.fillMaxWidth()
    ) {
        Text(text = "Eliminar completadas")
    }
}

@Preview(showBackground = true)
@Composable
fun DefaultPreview() {
    TaskApp()
}