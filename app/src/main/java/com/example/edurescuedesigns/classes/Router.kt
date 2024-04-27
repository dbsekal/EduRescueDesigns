package com.example.edurescuedesigns.classes

import android.util.Log
import com.example.edurescuedesigns.BuildConfig
import com.example.edurescuedesigns.datatypes.EmergencyPlan
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.Polyline
import com.google.android.gms.maps.model.PolylineOptions
import com.google.gson.Gson
import com.google.maps.android.PolyUtil
import okhttp3.Call
import okhttp3.Callback
import okhttp3.MediaType
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.RequestBody
import okhttp3.RequestBody.Companion.toRequestBody
import okhttp3.Response
import org.json.JSONObject
import java.io.IOException
import java.io.StringReader
import java.util.concurrent.CompletableFuture

class Router() {
    fun getRoute(currLocation: LatLng, destination: LatLng): CompletableFuture<PolylineOptions>  {
        val promise = CompletableFuture<PolylineOptions>()
        val currLat = currLocation.latitude
        val currLng = currLocation.longitude
        val destLat = destination.latitude
        val destLng = destination.longitude
        val requestBody = """
            {
              "origin":{
                "location":{
                  "latLng":{
                    "latitude": $currLat,
                    "longitude": $currLng
                  }
                }
              },
              "destination":{
                "location":{
                  "latLng":{
                    "latitude": $destLat,
                    "longitude": $destLng
                  }
                }
              },
              "travelMode": "WALK",
              "computeAlternativeRoutes": false,
              "routeModifiers": {
                "avoidTolls": false,
                "avoidHighways": false,
                "avoidFerries": false
              },
              "languageCode": "en-US",
              "units": "IMPERIAL"
            }
        """.trimIndent()
        val MEDIA_TYPE_MARKDOWN = "application/json".toMediaType()
        val url = "https://routes.googleapis.com/directions/v2:computeRoutes"
        val client = OkHttpClient()

        val requestBodyObject = requestBody.toRequestBody(MEDIA_TYPE_MARKDOWN)
        val key = BuildConfig.ROUTES_KEY



        val request = Request.Builder()
            .url(url)
            .header("Content-Type", "application/json")
            .header("X-Goog-Api-Key", key)
            .header("X-Goog-FieldMask", "routes.duration,routes.distanceMeters,routes.polyline.encodedPolyline")
            .post(requestBodyObject)
            .build()
        client.newCall(request).enqueue(object : Callback {
            override fun onResponse(call: Call, response: Response) {

                if (response.isSuccessful) {
                    val responseBody = response.body?.string()
                    Log.d("DEBUG", responseBody!!)
                    val polylineOptions = parseRouteResponse(responseBody!!)

                    // Invoke the callback with the polyline options
                    promise.complete(polylineOptions)

                } else {
                    val responseBody = response.body?.string()
                    Log.d("DEBUG FAIL", responseBody!!)
                    promise.completeExceptionally(IOException("Failed to fetch route data"))
                }
            }

            override fun onFailure(call: Call, e: IOException) {
                promise.completeExceptionally(IOException("Failed to fetch route data"))
            }
        })
        return promise
    }

    private fun parseRouteResponse(responseBody: String): PolylineOptions {
        val routesJsonArray = JSONObject(responseBody).optJSONArray("routes")
        val routeJsonObject = routesJsonArray?.optJSONObject(0)
        val encodedPolyline = routeJsonObject?.optJSONObject("polyline")?.optString("encodedPolyline") ?: ""
        // Decode the polyline to obtain LatLng points
        val decodedPolyline = PolyUtil.decode(encodedPolyline)
        // Create polyline options
        val polylineOptions = PolylineOptions()
        polylineOptions.addAll(decodedPolyline)

        return polylineOptions
    }

}