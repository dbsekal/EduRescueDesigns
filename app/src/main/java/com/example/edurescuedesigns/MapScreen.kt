package com.example.edurescuedesigns

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.viewinterop.AndroidView
import androidx.navigation.NavController
import com.example.edurescuedesigns.classes.MapsActivity

@Composable
fun MapScreen(navController: NavController) {
    val mapsActivity = remember { MapsActivity() } // Create an instance of MapsActivity
    DisposableEffect(Unit) {
        mapsActivity.initializeMap() // Initialize the map when the composable is first composed
        onDispose { /* Clean up if needed */ }
    }

    AndroidView(
        factory = { _ ->
            mapsActivity.binding.root // Return the root view of the MapsActivity layout
        },
        modifier = Modifier.fillMaxSize()
    )
}
