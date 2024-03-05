package com.example.edurescuedesigns

import android.util.Log
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import com.example.edurescuedesigns.classes.Network
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

@Composable
fun LoginForm(navController: NavController) {
    val email = remember { mutableStateOf("") }
    val password = remember { mutableStateOf("") }


    Column {
        Text(
            text = "EduRescue",
            style = MaterialTheme.typography.headlineLarge,
            modifier = Modifier.padding(8.dp)
        )
        EmailField(email.value) { email.value = it }
        PasswordField(email.value, password.value, { password.value = it },navController = navController)
        LoginButton(email.value, password.value, navController)
        RegisterButton()
    }


}

@Composable
fun EmailField(
    emailValue: String,
    onEmailChange: (String) -> Unit
) {
    OutlinedTextField(
        value = emailValue,
        onValueChange = onEmailChange,
        modifier = Modifier.padding(8.dp),
        label = { Text("Email") },
        keyboardOptions = KeyboardOptions.Default.copy(
            keyboardType = KeyboardType.Email,
            imeAction = ImeAction.Next
        ),
        keyboardActions = KeyboardActions(
            onNext = { /* Handle next action */ }
        )
    )
}

@Composable
fun PasswordField(
    emailValue: String,
    passwordValue: String,
    onPasswordChange: (String) -> Unit,
    navController: NavController
) {
    OutlinedTextField(
        value = passwordValue,
        onValueChange = onPasswordChange,
        modifier = Modifier.padding(8.dp),
        label = { Text("Password") },
        keyboardOptions = KeyboardOptions.Default.copy(
            keyboardType = KeyboardType.Password,
            imeAction = ImeAction.Done
        ),
        keyboardActions = KeyboardActions(
            onDone = {submitLogin(emailValue,passwordValue, navController)}
        )
    )
}

fun submitLogin(email: String, password: String,navController: NavController) {

    Network().login(email = email, password = password)
        .thenAccept{ response ->
            if (response.emailError) {
                Log.d("LOGIN RES",response.message)
            } else if (response.passwordError){
                Log.d("LOGIN RES", response.message)
            } else {
                Log.d("LOGIN RES", response.message)
                CoroutineScope(Dispatchers.Main).launch {
                    navController.navigate("homepage")
                }
            }

        }
}

@Composable
fun LoginButton(email: String, password: String,navController: NavController) {
    Button(onClick = {submitLogin(email,password, navController)}) {
        Text("Login")
    }
}

@Composable
fun RegisterButton() {
    Button(onClick = { /*TODO*/ }) {
        Text("Signup")

    }
}

@Preview
@Composable
fun PreviewLoginForm() {
    MaterialTheme {
        Surface {
            LoginForm(navController = rememberNavController())
        }
    }
}