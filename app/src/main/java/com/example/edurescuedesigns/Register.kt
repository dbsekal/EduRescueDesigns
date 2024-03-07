package com.example.edurescuedesigns

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBackIosNew
import androidx.compose.material3.Button
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController


@Composable
fun RegisterForm(navController: NavController) {
    val firstName = remember { mutableStateOf("")}
    val lastName = remember { mutableStateOf("")}
    val email = remember { mutableStateOf("")}
    val password = remember { mutableStateOf("")}
    val studentID = remember { mutableStateOf("")}

    Column(modifier = Modifier
        .fillMaxSize()
        .padding(40.dp),
        verticalArrangement = Arrangement.SpaceEvenly,
        horizontalAlignment = Alignment.Start

    ){
        IconButton(onClick = { navController.navigate("login") }) {
            Icon(imageVector = Icons.Filled.ArrowBackIosNew,
                contentDescription = "back_button"
            )
        }

        Text(
            modifier = Modifier.width(250.dp),
            text = "Create a new account",
            fontSize = 40.sp,
            fontWeight = FontWeight.Bold
        )
        FirstNameField(firstName.value) {firstName.value = it}
        LastNameField(lastName.value) {lastName.value = it}

    }

}

@Composable
fun FirstNameField(
    firstNameValue: String,
    onFirstNameChange: (String) -> Unit,
){
    OutlinedTextField(
        value = firstNameValue,
        onValueChange = onFirstNameChange,
        modifier = Modifier.padding(8.dp),
        label = { Text(text ="First Name") },
        keyboardOptions = KeyboardOptions.Default.copy(
            keyboardType = KeyboardType.Text,
            imeAction = ImeAction.Next
        ),
        keyboardActions = KeyboardActions(
            onNext = {}
        )
    )
}
@Composable
fun LastNameField(
    lastNameValue: String,
    onLastNameChange: (String) -> Unit
)
{
    OutlinedTextField(
        value = lastNameValue,
        onValueChange = onLastNameChange,
        modifier = Modifier.padding(8.dp),
        label = { Text(text ="First Name") },
        keyboardOptions = KeyboardOptions.Default.copy(
            keyboardType = KeyboardType.Text,
            imeAction = ImeAction.Next
        ),
        keyboardActions = KeyboardActions(
            onNext = {}
        )
    )
}

//@Composable
//fun EmailField(
//    emailValue: String,
//    onEmailChange = (String) -> Unit
//){
//    OutlinedTextField(
//        value = emailValue,
//        onValueChange = onEmailChange,
//        modifier = Modifier.padding(8.dp),
//        label = { Text(text ="Email") },
//        keyboardOptions = KeyboardOptions.Default.copy(
//            keyboardType = KeyboardType.Email,
//            imeAction = ImeAction.Next
//        ),
//        keyboardActions = KeyboardActions(
//            onNext = {}
//        )
//    )
//}
@Composable
fun RegisterButton(email: String, password: String,navController: NavController) {
    Button(onClick = {submitLogin(email,password, navController)}) {
        Text("Sign Up")
    }
}