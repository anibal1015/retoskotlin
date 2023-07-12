package com.example.myapplication
import android.content.Context

import kotlinx.coroutines.flow.*

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.*
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.*
import androidx.compose.material.*
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Checkbox
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.PreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import androidx.lifecycle.lifecycleScope
import com.example.myapplication.ui.theme.Blue
import com.example.myapplication.ui.theme.MyApplicationTheme
import com.example.myapplication.ui.theme.Teal
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.launch

class MainActivity : ComponentActivity() {
    private val dataStore: DataStore<Preferences> by preferencesDataStore(name = "settings")


    @OptIn(ExperimentalMaterial3Api::class)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            val todoList = remember { mutableStateListOf<TodoItemData>() }

            // Obtener los datos almacenados en el DataStore
            val savedTodoList = remember {
                val flow = dataStore.data.map { preferences ->
                    preferences[PreferencesKeys.TODO_LIST]?.split(",")?.map {
                        val (text, checked) = it.split(":")
                        TodoItemData(text, checked.toBoolean())
                    } ?: emptyList()
                }
                flow.first()
            }

            todoList.addAll(savedTodoList)

            val newTaskText = remember { mutableStateOf("") }

            MyApplicationTheme { // Usar el tema personalizado MyApplicationTheme en lugar de MaterialTheme
                Column(modifier = Modifier.fillMaxSize()) {
                    TopAppBar(
                        title = { Text(text = "Tareas") }
                    )
                    TextField(
                        value = newTaskText.value,
                        onValueChange = { newTaskText.value = it },
                        label = { Text(text = "Nueva tarea") },
                        modifier = Modifier.padding(16.dp),
                    )
                    Button(
                        onClick = {
                            val newTask = newTaskText.value.trim()
                            if (newTask.isNotEmpty()) {
                                todoList.add(TodoItemData(newTask, false))
                                newTaskText.value = ""
                                saveTodoList(todoList)
                            }
                        },
                        modifier = Modifier.align(Alignment.CenterHorizontally),
                        colors = ButtonDefaults.buttonColors(containerColor = Teal)
                    ) {
                        Text(text = "Agregar tarea")
                    }
                    LazyColumn(modifier = Modifier.weight(1f)) {
                        items(todoList) { todo ->
                            TodoItem(todo = todo) {
                                todoList.remove(todo)
                                saveTodoList(todoList)
                            }
                        }
                    }
                    Button(
                        onClick = {
                            todoList.removeAll { it.checked }
                            saveTodoList(todoList)
                        },
                        modifier = Modifier.align(Alignment.CenterHorizontally),
                        colors = ButtonDefaults.buttonColors(containerColor = Blue)
                    ) {
                        Text(text = "Eliminar tareas")
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
                    saveTodoList(todoList)
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

    object PreferencesKeys {
        val TODO_LIST = stringPreferencesKey("todo_list")
    }

    fun saveTodoList(todoList: List<TodoItemData>) {
        lifecycleScope.launch {
            dataStore.edit { preferences ->
                val serializedList = todoList.joinToString(",") { "${it.text}:${it.checked}" }
                preferences[PreferencesKeys.TODO_LIST] = serializedList
            }
        }
    }
}
