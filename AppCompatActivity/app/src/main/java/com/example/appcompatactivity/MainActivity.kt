import android.os.Bundle
import androidx.activity.compose.setContent
import androidx.appcompat.app.AppCompatActivity
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp

data class Task(val id: Long, val name: String, var isCompleted: Boolean)

class AppCompatActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            TaskApp()
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TaskApp() {
    val tasks = remember { mutableStateListOf<Task>() }
    val newTask = remember { mutableStateOf("") }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text(text = "Lista de Tareas") }
            )
        },
        content = {
            Column(modifier = Modifier.fillMaxSize()) {
                TaskInput(
                    taskName = newTask.value,
                    onTaskNameChange = { newTask.value = it },
                    onAddTask = {
                        tasks.add(Task(System.currentTimeMillis(), it.toString(), false))
                        newTask.value = ""
                    }
                )
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

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TaskInput(taskName: String, onTaskNameChange: (String) -> Unit, onAddTask: () -> Unit) {
    TextField(
        value = taskName,
        onValueChange = onTaskNameChange,
        label = { Text("Nueva Tarea") },
        modifier = Modifier.fillMaxWidth(),
        singleLine = true,
        keyboardActions = KeyboardActions(
            onDone = { onAddTask() }
        ),
        trailingIcon = {
            IconButton(onClick = { onAddTask() }) {
                Icon(Icons.Default.Add, contentDescription = "Agregar")
            }
        }
    )
}

@Composable
fun TaskList(tasks: List<Task>, onToggleTask: (Task) -> Unit) {
    LazyColumn(modifier = Modifier.fillMaxSize()) {
        items(tasks) { task ->
            Row(modifier = Modifier.fillMaxWidth()) {
                Checkbox(
                    checked = task.isCompleted,
                    onCheckedChange = { onToggleTask(task) },
                    modifier = Modifier.padding(start = 16.dp)
                )
                Text(
                    text = task.name,
                    style = MaterialTheme.typography.bodyLarge,
                    modifier = Modifier.weight(1f)
                )
            }
        }
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
