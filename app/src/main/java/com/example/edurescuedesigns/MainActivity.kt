package com.example.edurescuedesigns
import ChatRoomScreen
import android.content.Context
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.activity.compose.*
import androidx.compose.material3.MaterialTheme
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController


class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        ContextSingleton.initialize(this)
        //REMOVE THIS LINE TO SAVE LOGIN DATA
        Network().removeToken()
        setContent {
            MaterialTheme {
                val navController = rememberNavController()

                NavHost(navController = navController, startDestination = "login" ){
                    composable(route="login"){
                        LoginForm(navController)
                    }
                    composable(route="chatroom"){
                        ChatRoomScreen()
                    }
                    composable(route="homepage"){
                        HomePage()
                    }
                }
            }
        }
    }
}



