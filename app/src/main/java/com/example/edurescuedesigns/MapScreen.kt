import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.util.Log
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.layout.*
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationServices
import com.google.android.gms.maps.model.CameraPosition
import com.google.android.gms.maps.model.LatLng
import com.google.maps.android.compose.*

@SuppressLint("MissingPermission")
@Composable
fun MapScreen() {
    val context = LocalContext.current
    var currentLocation by remember { mutableStateOf(LatLng(33.7838, -118.1140)) }  // Default to a central location

    val rendezvousLocation = LatLng(33.78332703222142, -118.11434058125548)

    val cameraPositionState = rememberCameraPositionState {
        position = CameraPosition.builder()
            .target(rendezvousLocation)  // Sets the initial position of the camera
            .zoom(20f)                   // Sets the zoom level for close-up street views
            .tilt(50f)                   // Sets the tilt degree to give a 3D effect, useful for walking
            .build()
    }

    // Request location permission
    val permissionLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.RequestPermission(),
        onResult = { isGranted: Boolean ->
            if (isGranted) {
                val fusedLocationProviderClient: FusedLocationProviderClient =
                    LocationServices.getFusedLocationProviderClient(context)
                val locationResult = fusedLocationProviderClient.lastLocation
                locationResult.addOnSuccessListener { location ->
                    if (location != null) {
                        currentLocation = LatLng(location.latitude, location.longitude)
                    }
                }
            } else {
                Log.e("LocationPermission", "Permission Denied")
            }
        }
    )

    LaunchedEffect(key1 = true) {
        permissionLauncher.launch(android.Manifest.permission.ACCESS_FINE_LOCATION)
    }

    Column(modifier = Modifier.fillMaxSize()) {
        GoogleMap(
            modifier = Modifier.weight(1f),
            cameraPositionState = cameraPositionState
        ) {
            Marker(
                state = MarkerState(position = rendezvousLocation),
                title = "Rendezvous Point",
                snippet = "Horn Center"
            )
            Marker(
                state = MarkerState(position = currentLocation),
                title = "Your Location",
                snippet = "Current Location"
            )
        }
        // Always visible "Get Directions" Button
        Button(
            onClick = {
                openGoogleMaps(context, currentLocation, rendezvousLocation)
            },
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp)
        ) {
            Text("Get Directions")
        }
    }
}

fun openGoogleMaps(context: Context, from: LatLng, to: LatLng) {
    val uri = Uri.parse("https://maps.google.com/maps?saddr=${from.latitude},${from.longitude}&daddr=${to.latitude},${to.longitude}")
    val intent = Intent(Intent.ACTION_VIEW, uri)
    intent.setPackage("com.google.android.apps.maps")
    if (intent.resolveActivity(context.packageManager) != null) {
        context.startActivity(intent)
    } else {
        Log.e("Intent Error", "No Application can handle this request. Please install Google Maps.")
    }
}
