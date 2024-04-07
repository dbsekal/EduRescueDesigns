package com.example.edurescuedesigns

import android.text.Layout
import android.widget.Switch
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.tooling.preview.Preview
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import androidx.compose.material3.Switch
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Modifier
import androidx.compose.ui.Alignment
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.preference.PreferenceManager
import com.example.edurescuedesigns.ui.theme.*
import com.google.android.material.color.utilities.DynamicColor


//@Composable
//fun Settings(navController: NavController){
////    Text("Settings page")
//    var notificationsEnabled by remember { mutableStateOf(true) }
//    val isDarkModeInitialValue = isSystemInDarkTheme()
//    val isDarkMode = remember { mutableStateOf(isDarkModeInitialValue) }
//
//    // Function to toggle notifications
//    val toggleNotifications: (Boolean) -> Unit = { enabled ->
//        notificationsEnabled = enabled
//        // Perform any other actions when toggling notifications
//    }
//
//    val toggleDarkMode: (Boolean) -> Unit = { enabled ->
//        isDarkMode.value = enabled
//        AppTheme(
//            darkTheme = isDarkMode.value,
//            dynamicColor = DynamicColor,
//            content = {}
//        )
//    }
//
//
//    // UI for the settings page
//    Column(
//        horizontalAlignment = Alignment.CenterHorizontally,
//        modifier = Modifier.fillMaxWidth()
//    ) {
//        Spacer(modifier = Modifier.height(16.dp))
//        Text("Settings", fontSize = 30.sp)
//        // Toggle for notifications
//        Row(verticalAlignment = Alignment.CenterVertically) {
//            Text("Notifications")
//            Spacer(modifier = Modifier.weight(1f))
//            Switch(
//                checked = notificationsEnabled,
//                onCheckedChange = toggleNotifications,
//                modifier = Modifier.size(10.dp)
//            )
//        }
//        HorizontalDivider(modifier = Modifier.fillMaxWidth().padding(vertical = 8.dp))
//        Row(verticalAlignment = Alignment.CenterVertically) {
//            Text("Dark Mode")
//            Spacer(modifier = Modifier.weight(1f))
//            Switch(
//                checked = isDarkMode.value,
//                onCheckedChange = toggleDarkMode,
//                modifier = Modifier.size(10.dp)
//            )
//        }
//    }
//}
//
//@Preview(showBackground = true)
//@Composable
//fun DefaultPreview(){
//    Settings(navController = rememberNavController())
//}

//@Composable
//fun saveThemePreference(isDarkTheme: Boolean) {
//    if (isDarkTheme) {
//        PreferenceManager.getDefaultSharedPreferences(LocalContext.current.applicationContext).edit().putString(THEME_PREFS_KEY, "dark").apply()
//    } else {
//        PreferenceManager.getDefaultSharedPreferences(LocalContext.current.applicationContext).edit().putString(THEME_PREFS_KEY, "light").apply()
//    }
//}

@Composable
fun Settings(navController: NavController) {
    var notificationsEnabled by rememberSaveable { mutableStateOf(true) }
    val isDarkModeInitialValue = isSystemInDarkTheme()
    var isDarkMode by rememberSaveable { mutableStateOf(isDarkModeInitialValue) }

    val toggleNotifications: (Boolean) -> Unit = { enabled ->
        notificationsEnabled = enabled
        // Perform any other actions when toggling notifications
    }

    val toggleDarkMode: (Boolean) -> Unit = { enabled ->
        isDarkMode = enabled
    }

    Box(modifier = Modifier.fillMaxSize()) {
        AppTheme(
            darkTheme = isDarkMode,
            dynamicColor = false,
            content = {
                Column(
                    horizontalAlignment = Alignment.CenterHorizontally,
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Spacer(modifier = Modifier.height(16.dp))
                    Text("Settings", fontSize = 30.sp)
                    Spacer(modifier = Modifier.height(16.dp))
                    // Toggle for notifications
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        modifier = Modifier
                            .padding(start = 16.dp)
                            .padding(end = 30.dp)) {
                        Text("Notifications")
                        Spacer(modifier = Modifier.weight(0.5f))
                        Switch(
                            checked = notificationsEnabled,
                            onCheckedChange = toggleNotifications,
                            modifier = Modifier.size(5.dp)
                        )
                    }
                    HorizontalDivider(modifier = Modifier.fillMaxWidth().padding(vertical = 12.dp))
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        modifier = Modifier
                            .padding(start = 16.dp)
                            .padding(end = 30.dp)) {
                        Text("Dark Mode")
                        Spacer(modifier = Modifier.weight(0.5f))
                        Switch(
                            checked = isDarkMode,
                            onCheckedChange = toggleDarkMode,
                            modifier = Modifier.size(10.dp)
                        )
                    }
                }
            }
        )
    }
}

@Preview(showBackground = true)
@Composable
fun DefaultPreview() {
    Settings(navController = rememberNavController())
}