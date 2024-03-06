package com.example.edurescuedesigns
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.navigation.NavController

@Composable
fun HomePage(navController:NavController){
    Text("homepage")
    Button(onClick = { navController.navigate("chatroom") }) {
        Text("Chatroom")
    }

}