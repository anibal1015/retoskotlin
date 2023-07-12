package com.example.trabajoandroid

import androidx.compose.runtime.mutableStateListOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.createDataStore
import com.example.trabajoandroid.TodoListViewModel.TodoItem
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch

class TodoListViewModel : ViewModel() {
    private val todoList = mutableStateListOf<TodoItem>()
    private val dataStore: DataStore<Preferences> = createDataStore(name = "todo_list_prefs")
    private val completedTasksKey = stringPreferencesKey("completed_tasks")

    private val _todoListState = MutableStateFlow(todoList.toList())
    val todoListState = _todoListState.asStateFlow()

    init {
        loadTasksFromDataStore()
    }

    private fun loadTasksFromDataStore() {
        viewModelScope.launch(Dispatchers.IO) {
            dataStore.data.collect { preferences ->
                val completedTasks = preferences[completedTasksKey]?.split(",") ?: emptyList()
                todoList.forEach { it.isCompleted = it.id.toString() in completedTasks }
                _todoListState.value = todoList.toList()
            }
        }
    }

    private fun saveTasksToDataStore() {
        val completedTasks = todoList.filter { it.isCompleted }.joinToString(",") { it.id.toString() }
        viewModelScope.launch(Dispatchers.IO) {
            dataStore.edit { preferences ->
                preferences[completedTasksKey] = completedTasks
            }
        }
    }

    fun addTask(description: String) {
        val newTask = TodoItem(System.currentTimeMillis(), description)
        todoList.add(newTask)
        _todoListState.value = todoList.toList()
        saveTasksToDataStore()
    }

    fun toggleTaskCompleted(todoItem: TodoItem) {
        todoItem.isCompleted = !todoItem.isCompleted
        _todoListState.value = todoList.toList()
        saveTasksToDataStore()
    }

    fun deleteCompletedTasks() {
        todoList.removeAll { it.isCompleted }
        _todoListState.value = todoList.toList()
        saveTasksToDataStore()
    }
}
