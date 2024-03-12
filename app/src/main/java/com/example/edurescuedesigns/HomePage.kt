package com.example.edurescuedesigns
import android.util.Log
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AccountCircle
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import com.example.edurescuedesigns.classes.Network
import com.example.edurescuedesigns.datatypes.EmergencyPlan
import com.example.edurescuedesigns.datatypes.User
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

@Composable
fun HomePageForm(navController:NavController,shouldShowBottomBar: MutableState<Boolean>,userIsStudent:MutableState<Boolean>) {
    var user by remember { mutableStateOf(User()) }
    val emergencyPlan by remember{ mutableStateOf(EmergencyPlan())}
    var emergencyString by remember{ mutableStateOf("")}

    //What kind of user is it??
    LaunchedEffect(Unit) {
        if (!user.validToken) {
            // Fetch user info only if the user token is not valid
            Network().getUserInfo().thenAccept { userRes ->
                if (userRes.validToken) {
                    Log.d("Token RES", "valid")
                    user = userRes

                    if(user.type == "professor"){
                        userIsStudent.value = false
                    }

                    shouldShowBottomBar.value = true

                } else {
                    Log.d("Token RES", "invalid")
                    CoroutineScope(Dispatchers.Main).launch {
                        navController.navigate("login")
                    }
                }
            }

            Network().getEmergencyPlan().thenAccept { emergencyPlanRes ->
                emergencyPlan.is_active = emergencyPlanRes.is_active
                emergencyPlan.instructions = emergencyPlanRes.instructions
                if(emergencyPlanRes.is_active){
                    emergencyString = emergencyPlanRes.instructions
                }else{
                    emergencyString = "There is no emergency."
                }

            }


        }

    }
    topBar()
    Box( modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center){
        Text(text = emergencyString, fontSize = 40.sp, textAlign = TextAlign.Center)
    }

}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun topBar(){
    TopAppBar(
        title = {
            Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center){
                Text(text ="EduRescue", color = Color(0xFF930005), fontSize = 25.sp, fontFamily = FontFamily.Default, textAlign = TextAlign.Center)
            }
        },
        actions = {
            IconButton(onClick = { /*TODO*/ }) {
                Icon(Icons.Filled.AccountCircle, contentDescription = "User Profile")

            }
        }



    )
}

@Preview(showBackground = true)
@Composable
fun PreviewHomePage() {
    MaterialTheme {
        // Use a dummy NavController for preview purposes
        val navController = rememberNavController()
        // Create dummy MutableState<Boolean> for shouldShowBottomBar and userIsStudent
        val shouldShowBottomBar = remember { mutableStateOf(true) }
        val userIsStudent = remember { mutableStateOf(true) }

        Surface {
            HomePageForm(
                navController = navController,
                shouldShowBottomBar = shouldShowBottomBar,
                userIsStudent = userIsStudent
            )
        }
    }
}
