
import android.annotation.SuppressLint
import android.graphics.Bitmap
import android.graphics.Canvas
import android.graphics.Paint
import android.location.Location
import android.util.Log
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import com.example.edurescuedesigns.classes.Network
import com.example.edurescuedesigns.classes.Router
import com.example.edurescuedesigns.datatypes.User
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationServices
import com.google.android.gms.maps.model.BitmapDescriptor
import com.google.android.gms.maps.model.BitmapDescriptorFactory
import com.google.android.gms.maps.model.CameraPosition
import com.google.android.gms.maps.model.LatLng
import com.google.maps.android.compose.GoogleMap
import com.google.maps.android.compose.Marker
import com.google.maps.android.compose.MarkerState
import com.google.maps.android.compose.Polygon
import com.google.maps.android.compose.Polyline
import com.google.maps.android.compose.rememberCameraPositionState
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import android.graphics.Color as Color1


@SuppressLint("MissingPermission")
@Composable
fun MapScreen() {
    val context = LocalContext.current

    //TESTING LOCATIONS
    var japaneseGarden = LatLng(33.78579047152744, -118.11989238386515)
    var wellnessCenter = LatLng(33.785861807304734, -118.10869147957581)
    var closeToRend = LatLng(33.78393572046006, -118.11332633652312)
    var outsideCircle = LatLng(33.77690870302229, -118.06496074591993)
    //

    var defaultLocation = LatLng(33.785861807304734, -118.10869147957581)
    var rendezvousLocation by remember { mutableStateOf(defaultLocation)}

    var currentLocation by remember { mutableStateOf(defaultLocation) }
    var loading by remember { mutableStateOf(false) } // Loading state

    //FOR CSULB RADIUS
    var csulbPolygon = listOf(LatLng(33.78869735632184, -118.10399224951023),
        LatLng(33.77457218119482, -118.10399224951023),
        LatLng(33.77571369600366, -118.121330047839),
        LatLng(33.78851902267275, -118.12441995249166))

    //Justin - create a
    var polyLines by remember { mutableStateOf(listOf(currentLocation))}
    var directionsActive by remember {
        mutableStateOf((false))
    }


    //Checks if user is a professor or not -- Justin
    val userIsProfessor = remember { mutableStateOf(false) }
    var user by remember { mutableStateOf(User()) }

    //Professor location
    //Network call to get professor marked location
    var professorMarkerPosition by remember { mutableStateOf(LatLng(0.0, 0.0)) }
    var professorIcon by remember {
        mutableStateOf("")
    }
    var unMarked = LatLng(0.0,0.0)
    // Megan - Pops up dialog for when student is outside radius
    var showDialog by remember { mutableStateOf(false) }

    fun fetchUserInfo() {
        // Fetch user info only if the user token is not valid
        Network().getUserInfo().thenAccept { userRes ->
            if (userRes.validToken) {
                Log.d("Token RES", "valid")
                // Update user information
                user = userRes

                // Check if the user is a professor
                if(user.type == "professor") {
                    userIsProfessor.value = true
                }



            }
        }
    }

    //Grabs professors information
    fun fetchProfInfo() {
        // Fetch user info only if the user token is not valid
        Network().getProfessorData().thenAccept { professor ->
            if(userIsProfessor.value == false) {
                professorMarkerPosition = professor.second
                professorIcon = professor.first
                currentLocation = japaneseGarden
            }
            if(user.type == "test"){
                currentLocation = japaneseGarden
            }
        }
    }


    //Professor drops pin
    fun dropMarkerAtCurrentLocation() {
        professorMarkerPosition = currentLocation
        Network().setProfessorMarker(professorMarkerPosition)
    }

    //Function that detects if you are in CSULB
    fun pointInPolygon(point: LatLng, polygon: List<LatLng>): Boolean {
        // Initialize counter for intersections
        val currLat = point.latitude
        val currLng = point.longitude
        var intersections = 0

        // Iterate through each edge of the polygon
        for (i in polygon.indices) {
            val x1 = polygon[i].latitude
            val x2 = polygon[(i + 1) % polygon.size].latitude
            val y1 = polygon[i].longitude
            val y2 = polygon[(i + 1) % polygon.size].longitude

            // Check if the edge crosses the horizontal line at the y-coordinate of the point
            if (minOf(y1, y2) < currLat && currLng <= maxOf(y1, y2) && currLat <= maxOf(x1, x2)) {
                // Calculate the x-coordinate where the edge intersects with the horizontal line
                val xIntersect = (currLat - y1) * (x2 - x1) / (y2 - y1) + x1

                // Increment intersection counter if the x-coordinate is to the left of the point
                if (xIntersect <= currLat) {
                    intersections++
                }
            }
        }

        // If the number of intersections is odd, the point is inside the polygon
        return intersections % 2 == 1
    }

    var isWithinCSULB = pointInPolygon(currentLocation, csulbPolygon)

    //Network Call from emergency-plan

    val cameraPositionState = rememberCameraPositionState {
        position = CameraPosition.builder()
            .target(rendezvousLocation)
            .zoom(14.5f)
            .build()
    }

    //Justin Function that calls to routing api to find best route
    fun calculateRoute(currentLocation: LatLng, rendezvousLocation: LatLng) {
        directionsActive = true
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

    //Asks user if we can track location -- Justin
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
    //Creates a blue circle for current location for user - Justin
    fun createBlueCircleBitmapDescriptor(radius: Int): BitmapDescriptor {
        val bitmap = Bitmap.createBitmap(radius * 2, radius * 2, Bitmap.Config.ARGB_8888)
        val canvas = Canvas(bitmap)
        val paint = Paint().apply {
            isAntiAlias = true
            color = Color1.BLUE
        }
        canvas.drawCircle(radius.toFloat(), radius.toFloat(), radius.toFloat(), paint)
        return BitmapDescriptorFactory.fromBitmap(bitmap)
    }
    fun fetchCurrentLocation() {
        // You can use FusedLocationProviderClient to get the updated location
        val fusedLocationProviderClient: FusedLocationProviderClient =
            LocationServices.getFusedLocationProviderClient(context)
        fusedLocationProviderClient.lastLocation.addOnSuccessListener { location: Location? ->
            if (location != null) {
                currentLocation = LatLng(location.latitude, location.longitude)
            } else {
                Log.e("Location", "No location available")
            }
        }.addOnFailureListener { e ->
            Log.e("Location", "Failed to get location: ${e.message}")
        }

    }
    //Updates user's location - Justin
    fun updateCurrentLocation() {
        fetchCurrentLocation()
    }



    LaunchedEffect(key1 = true) {
        Network().getEmergencyPlan().thenAccept { emergencyPlanRes ->
            val coordinates = emergencyPlanRes.coordinates
            Log.d("MAP TEST", emergencyPlanRes.toString())
            rendezvousLocation = LatLng(coordinates.latitude.toDouble(), coordinates.longitude.toDouble())
        }
        permissionLauncher.launch(android.Manifest.permission.ACCESS_FINE_LOCATION)
        //Gets user info and professor info - justin
        fetchUserInfo()
        fetchProfInfo()
        fetchCurrentLocation()

        if (!isWithinCSULB) {
            showDialog = true
        }
    }

    //STYLING FOR PAGE

    //Styling using GoogleMap Api -- Justin
    Box(modifier = Modifier.fillMaxSize()) {
        GoogleMap(
            modifier = Modifier.fillMaxSize(),
            cameraPositionState = cameraPositionState
        ) {
            Marker(
                state = MarkerState(position = rendezvousLocation),
                title = "Rendezvous Point",
                snippet = "Meet here",
            )
            Marker(state = MarkerState(position = currentLocation), title = "Your Location",
                snippet = "Current Location", icon = createBlueCircleBitmapDescriptor(20)
            )
            if(professorMarkerPosition != unMarked) {
                Marker(
                    state = MarkerState(position = professorMarkerPosition),
                    title = "Professor's Location",
                    snippet = "Current Professor's Location",
                    alpha = 0.8F


                // You can define this function to create a custom marker icon
                )
            }

            Polyline(points = polyLines)
            Polygon(points = csulbPolygon, fillColor = Color(255, 99, 71, 30) )
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
            if (userIsProfessor.value) {
                Button(
                    onClick = { dropMarkerAtCurrentLocation() }
                ) {
                    Text(text = "Drop Marker")
                }
            }
            Button(
                onClick = { calculateRoute(currentLocation, rendezvousLocation) },
                enabled = isWithinCSULB
            ) {
                Text(text = "Get Directions", color = if (isWithinCSULB) Color.White else Color.Gray)
            }

            Button(
                onClick = {
                    updateCurrentLocation()
                    fetchProfInfo()
                    CoroutineScope(Dispatchers.Main).launch {
                        delay(150) 
                        if(directionsActive){
                            calculateRoute(currentLocation, rendezvousLocation)
                        } else{
                            cameraPositionState.position = CameraPosition.Builder()
                                .target(currentLocation)
                                .zoom(17f)
                                .build()

                        }

                    }
                }
            ) {
                Text(text = "Refresh")
            }
            //Megan - Shows message if user is outside of radius/not on campus
            if (showDialog) {
                AlertDialog(
                    onDismissRequest = { showDialog = false },
                    title = { Text(text = "Alert") },
                    text = {
                        Text(
                            text = "You are not within the CSULB area. Stay off campus until " +
                                    "in the case of emergency."
                        )
                    },

                    confirmButton = {
                        Button(
                            onClick = {
                                showDialog = false
                            }
                        ) {
                            Text(text = "OK")
                        }
                    }
                )
                }
            }

        }
}



