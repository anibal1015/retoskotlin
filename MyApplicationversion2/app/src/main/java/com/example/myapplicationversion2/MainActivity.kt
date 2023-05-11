package com.example.myapplicationversion2

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import com.example.myapplicationversion2.ui.theme.myapplicationversion2Theme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            MessageCard(name = "Lesley")

        }
    }
}
@Composable
fun MessageCard(name:String){
    Text("Hello $name!")
}
/*@Preview(showBackground = true)
@Composable
fun MessageCardPreview(){
    MessageCard(name = "Android")
}*/


data class Message(val author: String, val body: String)
@Composable
fun MessageCard(msg: Message){
    Column {
        Text(text = msg.author)
        Text(text = msg.body)
    }
}
@Preview(showBackground = true)
@Composable
fun MessageCardPreview(){
    MessageCard(msg = Message("Colleague",
        "Hey, take a look at Jetpack Compose, it's great!"))
}