package com.example.myapplication1

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.Column
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Row
import androidx.compose.ui.res.painterResource
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.unit.dp


class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            MessageCard(name = "Santiago")

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
    Row {
        Image(
            painter = painterResource(R.drawable.profile_picture),
            contentDescription = "Contact profile picture",
        )
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




