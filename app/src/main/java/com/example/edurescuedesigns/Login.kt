package com.example.edurescuedesigns

//import androidx.compose.foundation.layout.FlowColumnScopeInstance.align
import android.util.Log
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.text.ClickableText
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
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
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

    Box(modifier = Modifier.fillMaxSize()){
        Column (modifier = Modifier
            .align(Alignment.BottomCenter)
            .padding(20.dp)
        ){
            Text(
                text = "Don't have an account?",
                modifier = Modifier.align(Alignment.CenterHorizontally)
            )
            ClickableText(
                text = AnnotatedString("Sign Up Here"),
                modifier = Modifier.align(Alignment.CenterHorizontally),
                onClick = { navController.navigate("Register" ) },
                style = TextStyle(
                    fontSize = 14.sp,
                    fontFamily = FontFamily.Default,
                    textDecoration = TextDecoration.Underline,
                    color = Color(0xFFB81E19)))
        }
    }
    Column (modifier = Modifier.fillMaxWidth(),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            text = "EduRescue",
            style = MaterialTheme.typography.headlineLarge,
            modifier = Modifier.padding(8.dp),
            color = Color(0xFF410001)
        )
        EmailField(email.value) { email.value = it }
        PasswordField(email.value, password.value, { password.value = it },navController = navController)
        LoginButton(email.value, password.value, navController)
    }
}

@Composable
fun EmailField(
    emailValue: String,
    onEmailChange: (String) -> Unit
) {
    OutlinedTextField(
        value = emailValue,
        onValueChange = {input -> onEmailChange(input.replace("\\s".toRegex(), ""))},
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
        onValueChange = {input -> onPasswordChange(input.replace("\\s".toRegex(), ""))},
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


@Preview
@Composable
fun PreviewLoginForm() {
    MaterialTheme {
        Surface {
            LoginForm(navController = rememberNavController())
        }
    }
}