import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.lifecycle.viewmodel.compose.viewModelProvider
import androidx.lifecycle.viewmodel.compose.viewModelProviderFactory
import androidx.ui.tooling.preview.Devices
import androidx.ui.tooling.preview.PreviewDevice
import androidx.ui.tooling.preview.PreviewParameter
import kotlinx.coroutines.ExperimentalCoroutinesApi

@ExperimentalCoroutinesApi
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            TodoListApp()
        }
    }
}

@ExperimentalCoroutinesApi
@Composable
fun TodoListApp(todoListViewModel: TodoListViewModel = viewModel()) {
    val todoListState by todoListViewModel.todoListState.collectAsState()
    val focusManager = LocalFocusManager.current
    val textFieldValueState = rememberTextFieldValue()

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text(text = "Todo List") },
                elevation = 4.dp
            )
        },
        content = {
            Column(
                modifier = Modifier.padding(16.dp),
                verticalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                TextField(
                    value = textFieldValueState.text,
                    onValueChange = { textFieldValueState.text = it },
                    keyboardOptions = KeyboardOptions(imeAction = ImeAction.Done),
                    keyboardActions = KeyboardActions(onDone = { focusManager.clearFocus() }),
                    label = { Text(text = "New Task") }
                )
                Button(
                    onClick = {
                        todoListViewModel.addTask(textFieldValueState.text)
                        textFieldValueState.clear()
                    }
                ) {
                    Text(text = "Add Task")
                }
                TodoList(todoList = todoListState, todoListViewModel = todoListViewModel)
                Button(
                    onClick = { todoListViewModel.deleteCompletedTasks() },
                    modifier = Modifier.align(Alignment.End)
                ) {
                    Text(text = "Delete Completed")
                }
            }
        }
    )
}

@Composable
fun TodoList(todoList: List<TodoItem>, todoListViewModel: TodoListViewModel) {
    Column {
        for (todoItem in todoList) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Checkbox(
                    checked = todoItem.isCompleted,
                    onCheckedChange = { todoListViewModel.toggleTaskCompleted(todoItem) },
                    modifier = Modifier.padding(end = 8.dp)
                )
                Text(text = todoItem.description)
            }
        }
    }
}

@ExperimentalCoroutinesApi
@Preview(showBackground = true, device = Devices.DEFAULT)
@Composable
fun DefaultPreview() {
    TodoListApp()
}
