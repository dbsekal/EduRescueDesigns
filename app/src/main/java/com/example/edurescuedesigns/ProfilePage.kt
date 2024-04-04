package com.example.edurescuedesigns

import android.net.Uri
import android.widget.Toast
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Card
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.LocalTextStyle
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import coil.compose.rememberImagePainter

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun Profile(navController: NavController){

    val notification = rememberSaveable{mutableStateOf("")}
    if (notification.value.isNotEmpty()){
        Toast.makeText(LocalContext.current, notification.value, Toast.LENGTH_LONG).show()
        notification.value = ""
    }

    val context = LocalContext.current
    val name = rememberSaveable{ mutableStateOf("default name")}
    val email = rememberSaveable { mutableStateOf("default email")}
    val emergencycontact = rememberSaveable { mutableStateOf("")}
    val imageUri = rememberSaveable{ mutableStateOf("")}



    Column (modifier = Modifier
        .verticalScroll(rememberScrollState())
        .padding(8.dp)
    ){
    Row (
        modifier = Modifier
            .fillMaxWidth()
            .padding(8.dp),
        horizontalArrangement = Arrangement.SpaceBetween

    ){
        Text(text = "Cancel", modifier = Modifier.clickable{navController.navigate("homepage"); notification.value = "Cancelled"})
        Text(text = "Save", modifier = Modifier.clickable{

            navController.navigate("homepage");
            notification.value = "Profile updated"})

    }
        ProfileImage()

        NameInputRow(
            name = name.value,
            onNameChange = { name.value = it },
            modifier = Modifier.padding(start = 4.dp, end = 4.dp)
        )
        EmailInputRow(
            email = email.value,
            onEmailChange = { email.value = it },
            modifier = Modifier.padding(start = 4.dp, end = 4.dp)
        )
        EmergencyContact(
            emergencyContact = emergencycontact.value,
            onEmergencyContactChange = { emergencycontact.value = it },
            modifier = Modifier.padding(start = 4.dp, end = 4.dp)
        )


    }
}
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun NameInputRow(
    name: String,
    onNameChange: (String) -> Unit,
    modifier: Modifier = Modifier
) {
    Row(
        modifier = modifier
            .fillMaxWidth()
            .padding(start = 4.dp, end = 4.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(text = "Name", modifier = Modifier.width(100.dp))
        TextField(
            value = name,
            onValueChange = onNameChange,
            colors = TextFieldDefaults.textFieldColors(containerColor = Color.Transparent),
            textStyle = LocalTextStyle.current.copy(color = Color.Black)
        )
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun EmailInputRow(
    email: String,
    onEmailChange: (String) -> Unit,
    modifier: Modifier = Modifier
){
    Row(
        modifier = modifier
            .fillMaxWidth()
            .padding(start = 4.dp, end = 4.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(text = "Email", modifier = Modifier.width(100.dp))
        TextField(
            value = email,
            onValueChange = onEmailChange,
            colors = TextFieldDefaults.textFieldColors(containerColor = Color.Transparent),
            textStyle = LocalTextStyle.current.copy(color = Color.Black)
        )
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun EmergencyContact(
    emergencyContact: String,
    onEmergencyContactChange: (String) -> Unit,
    modifier: Modifier = Modifier){
    Row(
        modifier = modifier
            .fillMaxWidth()
            .padding(start = 4.dp, end = 4.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(text = "Emergency Contact Information", modifier = Modifier.width(100.dp))
        TextField(
            value = emergencyContact,
            onValueChange = onEmergencyContactChange,
            colors = TextFieldDefaults.textFieldColors(containerColor = Color.Transparent),
            textStyle = LocalTextStyle.current.copy(color = Color.Black),
            singleLine = false,
            modifier = Modifier.height(110.dp)

        )
    }

}


@Composable
fun ProfileImage(){
    val imageUri = rememberSaveable{ mutableStateOf("")}
    val painter = rememberImagePainter(
        if (imageUri.value.isEmpty())
        R.drawable.icon_account_circle
        else
        imageUri.value
    )
    val launcher = rememberLauncherForActivityResult(contract = ActivityResultContracts.GetContent()){
        uri: Uri? ->
        uri?.let{imageUri.value = it.toString()}
    }
    Column (modifier = Modifier
        .fillMaxWidth()
        .padding(8.dp), horizontalAlignment = Alignment.CenterHorizontally
    ){
        Card(shape = CircleShape, modifier = Modifier
            .padding(8.dp)
            .size(100.dp)) {
            Image(painter = painter, contentDescription = null, modifier = Modifier
                .wrapContentSize()
                .clickable { launcher.launch("image/*") },
                contentScale = ContentScale.Crop
            )
        }
        Text(text = "Change profile picture")
    }

}

@Preview(showBackground = true)
@Composable
fun DefaultPreview(){
    Profile(navController = rememberNavController())
}