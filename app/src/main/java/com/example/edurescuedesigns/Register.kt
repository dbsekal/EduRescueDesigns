@file:OptIn(ExperimentalMaterial3Api::class)

package com.example.edurescuedesigns

import android.util.Log
import android.widget.Toast
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBackIosNew
import androidx.compose.material.icons.filled.MoreVert
import androidx.compose.material3.Button
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.example.edurescuedesigns.classes.Network
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.DelicateCoroutinesApi
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch


@Composable
fun RegisterForm(navController: NavController) {
    val firstName = remember { mutableStateOf("")}
    val lastName = remember { mutableStateOf("")}
    val email = remember { mutableStateOf("")}
    val password = remember { mutableStateOf("")}
    val studentID = remember { mutableStateOf("")}
    val userSelection = remember { mutableStateOf("") }
    val scrollState = rememberScrollState()


    Column(modifier = Modifier
        .fillMaxSize()
        .padding(8.dp)
        .verticalScroll(scrollState),
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
            fontSize = 35.sp,
            fontWeight = FontWeight.Bold
        )
        FirstNameField(firstName.value) {firstName.value = it}
        LastNameField(lastName.value) {lastName.value = it}
        RegisterEmailField(email.value) {email.value = it}
        RegisterPasswordField(password.value) {password.value = it}
        DropDownUser()
//        DropDownUser(userSelection.value){userSelection.value = it}
        IDField(studentID.value) {studentID.value = it}
        // put a temporary type for now
        RegisterButton(firstName.value, lastName.value, email.value, password.value, studentID.value.toInt(), navController)
        //RegisterButton(firstName.value, lastName.value, navController)

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
        label = { Text(text ="Last Name") },
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
fun RegisterEmailField(
    emailValue: String,
    onEmailChange: (String) -> Unit
)
{
    OutlinedTextField(
        value = emailValue,
        onValueChange = onEmailChange,
        modifier = Modifier.padding(8.dp),
        label = { Text(text ="Email") },
        keyboardOptions = KeyboardOptions.Default.copy(
            keyboardType = KeyboardType.Email,
            imeAction = ImeAction.Next
        ),
        keyboardActions = KeyboardActions(
            onNext = {}
        )
    )
}

@Composable
fun RegisterPasswordField(
    passwordValue: String,
    onPasswordChange: (String) -> Unit
)
{
    OutlinedTextField(
        value = passwordValue,
        onValueChange = onPasswordChange,
        modifier = Modifier.padding(8.dp),
        label = { Text(text ="Password") },
        keyboardOptions = KeyboardOptions.Default.copy(
            keyboardType = KeyboardType.Password,
            imeAction = ImeAction.Next
        ),
        keyboardActions = KeyboardActions(
            onNext = {}
        )
    )
}
@Composable
fun DropDownUser() {
    val context = LocalContext.current
    var isExpanded = remember { mutableStateOf(false) }

    Box(
        modifier = Modifier.fillMaxWidth(),
        contentAlignment = Alignment.TopEnd
    ) {
        IconButton(onClick = { isExpanded.value = !isExpanded.value }) {
            Icon(imageVector = Icons.Default.MoreVert, contentDescription = "More")
        }
    }
    DropdownMenu(
        expanded = isExpanded.value,
        onDismissRequest = { isExpanded.value = false })
    {
        DropdownMenuItem(
            text = { Text("Admin") },
            onClick = { Toast.makeText(context, "Admin", Toast.LENGTH_SHORT).show() })
        DropdownMenuItem(
            text = { Text("Teacher") },
            onClick = { Toast.makeText(context, "Teacher", Toast.LENGTH_SHORT).show() })
        DropdownMenuItem(
            text = { Text("Student") },
            onClick = { Toast.makeText(context, "Student", Toast.LENGTH_SHORT).show() })

    }
}

//@Composable
//fun DropDownUser(
//    userValue: String,
//    onUserValueChange: (String) -> Unit
//) {
//    val isExpanded = remember { mutableStateOf(false) }
//    Box(
//        modifier = Modifier
//            .fillMaxWidth()
//            .padding(8.dp),
//    ) {
//        ExposedDropdownMenuBox(
//            expanded = isExpanded.value,
//            onExpandedChange = { isExpanded.value = it }
//        ) {
//            TextField(
//                value = userValue,
//                onValueChange = {onUserValueChange},
//                readOnly = true,
//                trailingIcon = {
//                    TrailingIcon(expanded = isExpanded.value)
//                },
//                colors = ExposedDropdownMenuDefaults.textFieldColors()
//            )
//
//            ExposedDropdownMenu(
//                expanded = isExpanded.value,
//                onDismissRequest = { isExpanded.value = false }
//            ) {
//                DropdownMenuItem(
//                    text = { Text("Admin") },
//                    onClick = {
//                        onUserValueChange("Admin")
//                        isExpanded.value = false
//                    }
//                )
//                DropdownMenuItem(
//                    text = { Text("Teacher") },
//                    onClick = {
//                        onUserValueChange("Teacher")
//                        isExpanded.value = false
//                    }
//                )
//                DropdownMenuItem(
//                    text = { Text("Student") },
//                    onClick = {
//                        onUserValueChange("Student")
//                        isExpanded.value = false
//                    }
//                )
//            }
//        }
//    }
//}

@Composable
fun IDField(
    idValue: String,
    onIDChange: (String) -> Unit
)
{
    OutlinedTextField(
        value = idValue,
        onValueChange = onIDChange,
        modifier = Modifier.padding(8.dp),
        label = { Text(text ="ID") },
        keyboardOptions = KeyboardOptions.Default.copy(
            keyboardType = KeyboardType.Number,
            imeAction = ImeAction.Next
        ),
        keyboardActions = KeyboardActions(
            onNext = {}
        )
    )
}

@Composable
fun RegisterButton(
    firstName: String,
    lastName: String,
    email: String,
    password: String,
    id: Int,
    navController: NavController
) {
    Button(
        onClick = {
            // Call the clickRegister function from within the composable function
            clickRegister(firstName, lastName, email, password, id, navController)
        }
    ) {
        Text("Sign Up")
    }
}

fun clickRegister(
    firstName: String,
    lastName: String,
    email: String,
    password: String,
    id: Int,
    navController: NavController
) {
    Log.d("HELP", "ran")
    Network().register(firstName, lastName, email, password, id)
        .thenAccept { response ->
            if (response.emailError) {
                // front end text for email is red
                Log.d("LOGIN RES", response.message)
            } else if (response.passwordError){
                // front end text for password is wrong
                Log.d("LOGIN RES", response.message)
            } else {
                Log.d("LOGIN RES", response.message)
                navController.navigate("login")
            }
        }
}



