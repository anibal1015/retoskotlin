package com.example.evaluacion_en_clase

import android.content.Context
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.Button
import androidx.compose.material.Checkbox

import androidx.compose.material.Text
import androidx.compose.material3.Button
import androidx.compose.material3.Checkbox
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import kotlinx.coroutines.flow.first
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import androidx.leanback.widget.Row
import com.example.evaluacion_en_clase.TaskDataStore.saveTasks
import com.example.evaluacion_en_clase.ui.theme.Evaluacion_en_claseTheme

val Context.dataStore by preferencesDataStore(name = "task_preferences")

fun preferencesDataStore(name: String): Any {

}

data class Task(val id: Int, val title: String, val completed: Boolean)

object TaskDataStore {
    suspend fun saveTasks(context: Context, tasks: List<Task>) {
        context.dataStore.edit { preferences ->
            val serializedTasks = tasks.joinToString(separator = ",") { task ->
                "${task.id},${task.title},${task.completed}"
            }
            preferences[PreferencesKeys.TASKS] = serializedTasks
        }
    }

    suspend fun loadTasks(context: Context): List<Task> {
        val preferences = context.dataStore.data.first()
        val serializedTasks = preferences[PreferencesKeys.TASKS] ?: ""
        return if (serializedTasks.isNotEmpty()) {
            serializedTasks.split(",").chunked(3) { (id, title, completed) ->
                Task(id.toInt(), title, completed.toBoolean())
            }
        } else {
            emptyList()
        }
    }

    private object PreferencesKeys {
        val TASKS = stringPreferencesKey("tasks")

        private fun stringPreferencesKey(s: String): Any {
            TODO("Not yet implemented")
        }
    }
}

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            TaskListScreen()
        }
    }
}

@Composable
fun TaskListScreen() {
    val tasks = remember { mutableStateListOf<Task>() }

    LaunchedEffect(Unit) {
        val applicationContext = null
        tasks.addAll(TaskDataStore.loadTasks(context = applicationContext))
    }

    val completedTasks = tasks.filter { it.completed }
    val pendingTasks = tasks.filter { !it.completed }
    val applicationContext
    Column {
        SearchBar(
            onAddTask = { task ->
                val newTask = Task(
                    id = tasks.size + 1,
                    title = task,
                    completed = false
                )
                tasks.add(newTask)
               
                saveTasks(context = applicationContext, tasks = tasks)
            }
        )
        LazyColumn {
            items(pendingTasks) { task ->
                TaskItem(task) { completed ->
                    task.dp = completed
                    saveTasks(context = applicationContext, tasks = tasks)
                }
            }
            item {
                Button(
                    onClick = {
                        tasks.removeAll(completedTasks)
                        saveTasks(context = applicationContext, tasks = tasks)
                    },
                    enabled = completedTasks.isNotEmpty()
                ) {
                    Text("Eliminar completadas")
                }
            }
        }
    }
}

@Composable
fun SearchBar(onAddTask: (String) -> Unit) {
    val textState = remember { mutableStateOf(TextFieldValue()) }

    Row {
        TextField(
            value = textState.value,
            onValueChange = { textState.value = it },
            label = { Text("Nueva tarea") },
            modifier = Modifier.weight(1f)
        )
        Button(
            onClick = {
                onAddTask(textState.value.text)
                textState.value = TextFieldValue()
            }
        ) {
            Text("Agregar")
        }
    }
}

@Composable
fun TaskItem(task: Task, onTaskCompleted: (Boolean) -> Unit) {
    Row(
        modifier = Modifier.padding(8.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Checkbox(
            checked = task.completed,
            onCheckedChange = { onTaskCompleted(it) },
            modifier = Modifier.padding(end = 8.dp)
        )
        Text(text = task.title)
    }
}

@Preview(showBackground = true)
@Composable
fun DefaultPreview() {
    Evaluacion_en_claseTheme {
        TaskListScreen()
    }
}
