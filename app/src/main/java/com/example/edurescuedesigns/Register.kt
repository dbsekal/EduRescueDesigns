@file:OptIn(ExperimentalMaterial3Api::class)

package com.example.edurescuedesigns

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBackIosNew
import androidx.compose.material.icons.filled.ArrowDropDown
import androidx.compose.material3.Button
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController


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
        DropDownUser(userSelection.value){userSelection.value = it}
        IDField(studentID.value) {studentID.value = it}
        RegisterButton()



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
fun DropDownUser(
    selectedValue: String,
    onSelectedValueChange: (String) -> Unit
) {
    val expanded = remember { mutableStateOf(false) }
    val options = listOf("Professor", "Student")
    val chosenOption = remember { mutableStateOf(selectedValue) }

    Column() {
        OutlinedTextField(
            value = chosenOption.value,
            onValueChange = { },
            readOnly = true, // Make the text field read-only
            label = { Text("Select User Type") },
            trailingIcon = {
                Icon(Icons.Filled.ArrowDropDown, "dropdown",
                    Modifier.clickable { expanded.value = true })
            },
            modifier = Modifier
                .padding(8.dp)
                .clickable { expanded.value = true } // Make the entire field clickable
        )
        DropdownMenu(
            expanded = expanded.value,
            onDismissRequest = { expanded.value = false }
        ) {
            options.forEach { option ->
                DropdownMenuItem(
                    text = { Text(option) },
                    onClick = {
                        expanded.value = false
                        chosenOption.value = option
                        onSelectedValueChange(option)
                    }
                )
            }
        }
    }
}




@Composable
fun IDField(
    iDValue: String,
    onIDChange: (String) -> Unit
)
{
    OutlinedTextField(
        value = iDValue,
        onValueChange = onIDChange,
        modifier = Modifier.padding(8.dp),
        label = { Text(text ="ID") },
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
fun RegisterButton() {
    Button(onClick = {/*todo*/}) {
        Text("Sign Up")
    }
}

@Preview
@Composable
fun PreviewRegister() {
    MaterialTheme {
        Surface {
            RegisterForm(navController = rememberNavController())
        }
    }
}