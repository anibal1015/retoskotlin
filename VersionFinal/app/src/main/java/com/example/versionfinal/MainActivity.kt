import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.*
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.*
import androidx.compose.material.*
import androidx.compose.material3.Button
import androidx.compose.material3.Checkbox
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp

class MainActivity : ComponentActivity() {
    @OptIn(ExperimentalMaterial3Api::class)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            val todoList = remember { mutableStateListOf(TodoItemData("Tarea 1", false), TodoItemData("Tarea 2", false), TodoItemData("Tarea 3", false)) }
            val newTaskText = remember { mutableStateOf("") }

            MaterialTheme {
                Column(modifier = Modifier.fillMaxSize()) {
                    TopAppBar(
                        title = { Text(text = "Tareas") }
                    )
                    TextField(
                        value = newTaskText.value,
                        onValueChange = { newTaskText.value = it },
                        label = { Text(text = "Nueva tarea") },
                        modifier = Modifier.padding(16.dp)
                    )
                    Button(
                        onClick = {
                            val newTask = newTaskText.value.trim()
                            if (newTask.isNotEmpty()) {
                                todoList.add(TodoItemData(newTask, false))
                                newTaskText.value = ""
                            }
                        },
                        modifier = Modifier.align(Alignment.CenterHorizontally)
                    ) {
                        Text(text = "Agregar tarea")
                    }
                    LazyColumn(modifier = Modifier.weight(1f)) {
                        items(todoList) { todo ->
                            TodoItem(todo = todo) {
                                todoList.remove(todo)
                            }
                        }
                    }
                    Button(
                        onClick = {
                            todoList.removeAll { it.checked }
                        },
                        modifier = Modifier.align(Alignment.CenterHorizontally)
                    ) {
                        Text(text = "Eliminar tareas")
                    }
                }
            }
        }
    }
}

@Composable
fun TodoItem(todo: TodoItemData, onTaskRemoved: () -> Unit) {
    var checkedState by remember { mutableStateOf(todo.checked) }

    LaunchedEffect(todo.checked) {
        checkedState = todo.checked
    }

    Row(modifier = Modifier.padding(16.dp)) {
        Checkbox(
            checked = checkedState,
            onCheckedChange = { isChecked ->
                checkedState = isChecked
                todo.checked = isChecked
            },
            modifier = Modifier.padding(end = 16.dp)
        )
        Text(text = todo.text)
    }
}

data class TodoItemData(
    val text: String,
    var checked: Boolean
)