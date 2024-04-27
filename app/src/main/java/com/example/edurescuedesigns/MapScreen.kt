import android.annotation.SuppressLint
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.net.Uri

import android.util.Log
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.layout.*
import androidx.compose.material3.Button
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.core.content.ContextCompat
import com.example.edurescuedesigns.R
import com.example.edurescuedesigns.classes.Router
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationServices
import com.google.android.gms.maps.model.BitmapDescriptor
import com.google.android.gms.maps.model.BitmapDescriptorFactory
import com.google.android.gms.maps.model.CameraPosition
import com.google.android.gms.maps.model.LatLng
import com.google.maps.android.compose.*
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import okhttp3.internal.wait


@SuppressLint("MissingPermission")
@Composable
fun MapScreen() {
    val context = LocalContext.current
    var currentLocation by remember { mutableStateOf(LatLng(33.785467362462754, -118.10923936183619)) }  // Default to a central location
    var loading by remember { mutableStateOf(false) } // Loading state

    var polyLines by remember { mutableStateOf(listOf(currentLocation))}



    val rendezvousLocation = LatLng(33.78332703222142, -118.11434058125548)
    val cameraPositionState = rememberCameraPositionState {
        position = CameraPosition.builder()
            .target(rendezvousLocation)
            .zoom(15f)
            .build()
    }


    fun calculateRoute(currentLocation: LatLng, rendezvousLocation: LatLng) {
        loading = true
        Router().getRoute(currentLocation, rendezvousLocation).thenApply { polylineOptions ->
            loading = false
            polyLines = polylineOptions.points
            Log.d("POLY 2", polyLines.toString())
        }
        cameraPositionState.position = CameraPosition.Builder()
            .target(currentLocation)
            .zoom(17f)
            .build()
    }

    val permissionLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.RequestPermission(),
        onResult = { isGranted: Boolean ->
            if (isGranted) {
                val fusedLocationProviderClient: FusedLocationProviderClient =
                    LocationServices.getFusedLocationProviderClient(context)
                val locationResult = fusedLocationProviderClient.lastLocation
                locationResult.addOnSuccessListener { location ->
                    if (location != null) {
                        //Uncomment to get currentLocation
                        //currentLocation = LatLng(location.latitude, location.longitude)
                    }
                }
            } else {
                Log.e("LocationPermission", "Permission Denied")
            }
        }
    )

    fun createBlueCircleBitmapDescriptor(radius: Int): BitmapDescriptor {
        val bitmap = Bitmap.createBitmap(radius * 2, radius * 2, Bitmap.Config.ARGB_8888)
        val canvas = Canvas(bitmap)
        val paint = Paint().apply {
            isAntiAlias = true
            color = Color.BLUE
        }
        canvas.drawCircle(radius.toFloat(), radius.toFloat(), radius.toFloat(), paint)
        return BitmapDescriptorFactory.fromBitmap(bitmap)
    }

    fun updateCurrentLocation() {
        // You can use FusedLocationProviderClient to get the updated location
        val fusedLocationProviderClient: FusedLocationProviderClient =
            LocationServices.getFusedLocationProviderClient(context)
        val locationResult = fusedLocationProviderClient.lastLocation
        locationResult.addOnSuccessListener { location ->
            if (location != null) {
                currentLocation = LatLng(location.latitude, location.longitude)
                // You may want to update the camera position as well

        }

        }
    }

    LaunchedEffect(key1 = true) {
        permissionLauncher.launch(android.Manifest.permission.ACCESS_FINE_LOCATION)
    }

    Box(modifier = Modifier.fillMaxSize()) {
        GoogleMap(
            modifier = Modifier.fillMaxSize(),
            cameraPositionState = cameraPositionState
        ) {
            Marker(
                state = MarkerState(position = rendezvousLocation),
                title = "Rendezvous Point",
                snippet = "Horn Center",
            )
            Marker(state = MarkerState(position = currentLocation), title = "Your Location",
                snippet = "Current Location", icon = createBlueCircleBitmapDescriptor(20)
            )
            Polyline(points = polyLines)
        }

        if (loading) {
            Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                CircularProgressIndicator()
            }
        }

        Column(
            modifier = Modifier
                .align(Alignment.TopEnd)
        ) {
            Button(onClick = { calculateRoute(currentLocation,rendezvousLocation) }) {
                Text(text = "Get Directions")
            }
            Button(onClick = {
                updateCurrentLocation()

                CoroutineScope(Dispatchers.Main).launch {
                    delay(150) // Adjust the delay time as needed
                    calculateRoute(currentLocation, rendezvousLocation)
                }
            }) {
                Text(text = "Refresh Location")
            }

        }
    }
}


