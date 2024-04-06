package com.example.edurescuedesigns

import android.net.Uri
import android.util.Log
import android.widget.Toast
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
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
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import coil.compose.AsyncImage
import coil.compose.AsyncImagePainter
import coil.compose.rememberImagePainter
import com.example.edurescuedesigns.classes.Network
import com.example.edurescuedesigns.datatypes.User
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun Profile(navController: NavController){
    var user by remember { mutableStateOf(User()) }

    val firstName = rememberSaveable{ mutableStateOf("")}
    val lastName = rememberSaveable{ mutableStateOf("")}

    val email = rememberSaveable { mutableStateOf("")}
    var imageUri = rememberSaveable{ mutableStateOf("")}

    //Ensures that the fetch request is only done iff user needs validation
    LaunchedEffect(Unit) {
        if (!user.validToken) {
            // Fetch user info only if the user token is not valid
            Network().getUserInfo().thenAccept { userRes ->
                if (userRes.validToken) {
                    Log.d("Token RES", userRes.toString())
                    user = userRes
                    firstName.value = user.firstName
                    lastName.value = user.lastName
                    email.value = user.email
                    imageUri.value = user.profilepic
                } else {
                    Log.d("Token RES", "invalid")
                    CoroutineScope(Dispatchers.Main).launch {
                        navController.navigate("login")
                    }
                }
            }


        }
    }
    val notification = rememberSaveable{mutableStateOf("")}
    if (notification.value.isNotEmpty()){
        Toast.makeText(LocalContext.current, notification.value, Toast.LENGTH_LONG).show()
        notification.value = ""
    }

    val context = LocalContext.current




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
            Network().updateUser(user)
            navController.navigate("homepage");
            notification.value = "Profile updated"})

    }
        ProfileImage(imageUri = imageUri)

        NameInputRow(
            name = firstName.value,
            onNameChange = { firstName.value = it },
            modifier = Modifier.padding(start = 4.dp, end = 4.dp),
            isFirst = true
        )

        NameInputRow(
            name = lastName.value,
            onNameChange = { lastName.value = it },
            modifier = Modifier.padding(start = 4.dp, end = 4.dp),
            isFirst = false
        )
        EmailInputRow(
            email = email.value,
            onEmailChange = { email.value = it },
            modifier = Modifier.padding(start = 4.dp, end = 4.dp)
        )


    }
}
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun NameInputRow(
    name: String,
    onNameChange: (String) -> Unit,
    modifier: Modifier = Modifier,
    isFirst: Boolean
) {
    val label = if (isFirst) "First Name" else "Last Name"
    Row(
        modifier = modifier
            .fillMaxWidth()
            .padding(start = 4.dp, end = 4.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(text = label, modifier = Modifier.width(100.dp))
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



@Composable
fun ProfileImage(imageUri: MutableState<String>) {

    val launcher = rememberLauncherForActivityResult(contract = ActivityResultContracts.GetContent()) { uri: Uri? ->
        uri?.let {
            // Update the imageUri with the URI of the selected image
            imageUri.value = it.toString()
        }
    }

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(8.dp), horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Card(
            shape = CircleShape, modifier = Modifier
                .padding(8.dp)
                .size(100.dp)
        ) {
            AsyncImage(
                model = imageUri.value,
                contentDescription = null,
                modifier = Modifier
                    .fillMaxSize()
                    .clip(CircleShape)
                    .clickable { launcher.launch("image/*") }
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