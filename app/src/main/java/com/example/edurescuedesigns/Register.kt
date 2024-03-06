package com.example.edurescuedesigns

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController

@Composable
fun RegisterForm(navController: NavController) {
    var firstName = remember { mutableStateOf("")}
    var lastName = remember { mutableStateOf("")}
    var email = remember { mutableStateOf("")}
    var password = remember { mutableStateOf("")}
    var studentID = remember { mutableStateOf("")}

    Column(modifier = Modifier
        .fillMaxSize()
        .padding(40.dp),
        verticalArrangement = Arrangement.SpaceEvenly,
        horizontalAlignment = Alignment.Start

    ){

    }

}

@Composable
fun RegisterButton(email: String, password: String,navController: NavController) {
    Button(onClick = {submitLogin(email,password, navController)}) {
        Text("Sign Up")
    }
}