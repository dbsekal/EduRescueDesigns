package com.example.edurescuedesigns
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import androidx.activity.compose.*
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
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import okhttp3.*
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.RequestBody.Companion.toRequestBody
import java.io.IOException


class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            MaterialTheme {
                Surface {
                    LoginForm()
                }
            }
        }
    }
}

@Composable
fun LoginForm() {
    val email = remember { mutableStateOf("") }
    val password = remember { mutableStateOf("") }

    Column {
        EmailField(email.value) { email.value = it }
        PasswordField(email.value, password.value) { password.value = it }
        LoginButton(email.value, password.value)
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
    onPasswordChange: (String) -> Unit
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
            onDone = {submitLogin(emailValue,passwordValue)}
        )
    )
}

fun submitLogin(email: String, password: String) {
    Network().login(email = email, password = password)
}

@Composable
fun LoginButton(email: String, password: String) {



    Button(onClick = {submitLogin(email,password)}) {
        Text("Login")
    }
}

@Preview
@Composable
fun PreviewLoginForm() {
    MaterialTheme {
        Surface {
            LoginForm()
        }
    }
}