package com.example.edurescuedesigns
import ChatRoomScreen
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import androidx.activity.compose.*
import androidx.compose.material3.MaterialTheme
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.edurescuedesigns.classes.ContextSingleton
import com.example.edurescuedesigns.classes.Network
import com.example.edurescuedesigns.ui.theme.AppTheme


class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        ContextSingleton.initialize(this)
        //REMOVE THIS LINE TO SAVE LOGIN DATA

       Network().removeToken()

        //Check if user is already logged in
        var startDestination:String = "login"

        Network().getUserInfo().thenAccept { userRes ->
            if (userRes.validToken) {
                Log.d("Token RES", "valid")
                startDestination = "homepage"
            } else {
                    Log.d("Token RES", "invalid")
            }
        }



        setContent {
            AppTheme {
                val navController = rememberNavController()

                NavHost(
                    navController = navController,
                    startDestination = startDestination){
                    composable(route="login"){
                        LoginForm(navController)
                    }
                    composable(route="chatroom"){
                        ChatRoomScreen(navController = navController)
                    }
                    composable(route = "register"){
                        RegisterForm(navController)
                    }
                    composable(route="homepage"){
                        HomePageForm(navController)
                    }
                }
            }
        }
    }
}



