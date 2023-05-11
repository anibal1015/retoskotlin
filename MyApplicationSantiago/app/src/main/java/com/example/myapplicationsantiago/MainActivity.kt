package com.example.myapplication

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import com.example.myapplication.ui.theme.MyApplicationTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            MessageCard(name = "2503804")
        }
    }
}
data class Message(val author: String, val body: String)


@Composable
fun MessageCard(msg : Message){
    Text(text = msg.author)
    Text(text = msg.body)
}
@Preview(showBackground = true)
@Composable
fun MessageCardPreview(){
    MessageCard(msg = Message("hola mundo, care chumba hpta egh. solo santa fe locas"))
}
