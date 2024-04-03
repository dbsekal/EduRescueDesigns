package com.example.edurescuedesigns

import android.Manifest
import android.os.Build
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Button
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.google.accompanist.permissions.ExperimentalPermissionsApi
import com.google.accompanist.permissions.isGranted
import com.google.accompanist.permissions.rememberPermissionState




@OptIn(ExperimentalPermissionsApi::class)
@Composable
fun MapScreen(navController:NavController){


    Text("Map")

    val context = LocalContext.current
    val notificationService = NotificationService(context)
    Box(
        modifier = Modifier.fillMaxSize(),
        contentAlignment = Alignment.Center
    ) {
        Column (
            modifier = Modifier.fillMaxWidth(),
            horizontalAlignment = Alignment.CenterHorizontally
        ){
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU){
                val permissionState = rememberPermissionState(
                    permission = Manifest.permission.POST_NOTIFICATIONS
                )
                if (!permissionState.status.isGranted) {
                    OutlinedButton(onClick = { /*TODO*/ }) {
                        Text(text = "Allow Notification", fontSize = 22.sp)

                    }
                }
            }
            Spacer(modifier = Modifier.size(16.dp))
            Button(onClick = { notificationService.showNotification() }) {
                Text(text = "Show Notification", fontSize = 22.sp)

            }
        }
    }
}