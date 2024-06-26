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
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Modifier
import androidx.compose.ui.Alignment
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.preference.PreferenceManager
import com.example.edurescuedesigns.ui.theme.*



@Composable
fun Settings(navController: NavController, viewModel: ThemeViewModel) {
    // Save user's preferences after navigating to a different page in app
    val sharedPreferences = PreferenceManager.getDefaultSharedPreferences(LocalContext.current)
    var notificationsEnabled by remember { mutableStateOf(sharedPreferences.getBoolean("notifications", true)) }
    val isDarkModeInitialValue = isSystemInDarkTheme()
    var darkModeToggle by remember { mutableStateOf(sharedPreferences.getBoolean("darkMode", isDarkModeInitialValue)) }

    // Save pref to SharedPreferences
    fun savePreferences() {
        with(sharedPreferences.edit()) {
            putBoolean("notifications", notificationsEnabled)
            putBoolean("darkMode", darkModeToggle)
            apply()
        }
    }

    val toggleNotifications: (Boolean) -> Unit = { enabled ->
        notificationsEnabled = enabled
        savePreferences()
    }

    // calling the viewmodel theme for dark mode
    val toggleDarkMode: (Boolean) -> Unit = { enabled ->
        darkModeToggle = enabled
        viewModel.toggleDarkMode()
        savePreferences()
    }

    Box(modifier = Modifier.fillMaxSize()) {
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
                    checked = darkModeToggle,
                    onCheckedChange = toggleDarkMode,
                    modifier = Modifier.size(10.dp)
                )
            }
            HorizontalDivider(modifier = Modifier.fillMaxWidth().padding(vertical = 12.dp))
            Row(
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier
                    .padding(start = 16.dp)
                    .padding(end = 30.dp)) {
                Text("Languages")
                Spacer(modifier = Modifier.weight(0.5f))
            }
        }
    }
}

