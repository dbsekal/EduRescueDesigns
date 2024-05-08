package com.example.edurescuedesigns.classes

import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import com.example.edurescuedesigns.datatypes.ChatMessage
import com.example.edurescuedesigns.datatypes.EmergencyPlan
import com.example.edurescuedesigns.datatypes.LoginResponse
import com.example.edurescuedesigns.datatypes.ProfessorData
import com.example.edurescuedesigns.datatypes.User
import com.google.android.gms.maps.model.LatLng
import com.google.gson.Gson
import okhttp3.Call
import okhttp3.Callback
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.RequestBody.Companion.toRequestBody
import okhttp3.Response
import org.json.JSONObject
import java.io.IOException
import java.io.StringReader
import java.util.concurrent.CompletableFuture


class Network {
    private val MEDIA_TYPE_MARKDOWN = "application/json".toMediaType()
    private val Client = OkHttpClient()
    private val context = ContextSingleton.getInstance().getContext()
    private val sharedpref = context.getSharedPreferences("preferences", AppCompatActivity.MODE_PRIVATE)
    /*TODO - Have both the login function and register function return helpful error information
       For example: if the password is wrong, send a boolean passwordError: true so that the UI can
       prompt the user accordingly*/

    /*TODO - This may require you to change the Node.js server to return what you want*/
    fun login(email: String, password: String) : CompletableFuture<LoginResponse>{
        val promise = CompletableFuture<LoginResponse>()
        try {
            Log.d("LOGIN ATTEMPT:", "$email --- $password")
            val url = "http://10.0.2.2:8008/account/login"
            val json = """
            {
                "email": "$email",
                "password": "$password"
            }
        """.trimIndent()

            val request = Request.Builder().url(url).post(json.toRequestBody(MEDIA_TYPE_MARKDOWN)).build()
            //This enqueue function is sortof like async in JavaScript

            Client.newCall(request).enqueue(object : Callback {
                override fun onResponse(call: Call, response: Response) {
                    var responseBody = response.body!!.string()
                    val gson = Gson()
                    val loginResponse = gson.fromJson(StringReader(responseBody), LoginResponse::class.java)
                    if (!response.isSuccessful) {
                        promise.complete(loginResponse)
                    } else {
                        setToken(loginResponse.token)
                        promise.complete(loginResponse)
                        Log.d("LOGIN ATTEMPT:", responseBody)
                    }
                }

                override fun onFailure(call: Call, e: IOException) {
                    Log.e("LOGIN ATTEMPT:", "Error: ${e.message}")
                }
            })
        } catch (e: Exception) {
            Log.e("LOGIN ATTEMPT:", "Error: ${e.message}")
        }

        return promise
    }
    /*TODO- Create a register function that hits our API*/

    fun updateUser(user:User){
        //TODO UPDATE USER
        return
    }


    fun setToken(token:String){
        val editor = sharedpref.edit()
        editor.putString("token", token).apply()
    }
    fun getUserToken():String{
        val token = sharedpref.getString("token", null)
        if(token != null) {
            Log.d("TOKEN VAL: ", token)
            return token
        }
        Log.d("TOKEN VAL: ", "unintialized")
        return ""

    }
    fun removeToken() {
        val editor = sharedpref.edit()
        editor.remove("token").apply()
    }


    //getUserInfo sends the backend a JWT token to both see if user is authorized and also
    //Returns that users info - Justin

    fun getEmergencyPlan():CompletableFuture<EmergencyPlan>{
        val promise = CompletableFuture<EmergencyPlan>()
        val token = getUserToken();

        try {
            val url = "http://10.0.2.2:8008/emergencyplan/user"
            val request = Request.Builder()
                .url(url)
                .header("Authorization", "Bearer $token")
                .build()
            Client.newCall(request).enqueue(object : Callback {
                override fun onResponse(call: Call, response: Response) {
                    var responseBody = response.body!!.string()
                    Log.d("emergency2", responseBody)
                    val gson = Gson()
                    val emergencyPlan = gson.fromJson(StringReader(responseBody), EmergencyPlan::class.java)
                    if (!response.isSuccessful) {
                        promise.complete(emergencyPlan)
                    } else {
                        promise.complete(emergencyPlan)
                        Log.d("EMERGENCY", responseBody)
                    }
                }

                override fun onFailure(call: Call, e: IOException) {
                    Log.e("JUSTIN", "Error: ${e.message}")
                }
            })
        } catch (e: Exception) {
            Log.e("JUSTIN", "Error: ${e.message}")
        }
        return promise
    }



    fun getUserInfo(): CompletableFuture<User> {
        val promise = CompletableFuture<User>()

        val token = getUserToken()

        try {
            val url = "http://10.0.2.2:8008/account/getinfo"
            val request = Request.Builder()
                .url(url)
                .header("Authorization", "Bearer $token")
                .build()
            Client.newCall(request).enqueue(object : Callback {
                override fun onResponse(call: Call, response: Response) {
                    var responseBody = response.body!!.string()
                    val gson = Gson()
                    val user = gson.fromJson(StringReader(responseBody), User::class.java)
                    if (!response.isSuccessful) {
                        promise.complete(user)
                    } else {
                        promise.complete(user)
                        Log.d("JUSTIN", responseBody)
                    }
                }

                override fun onFailure(call: Call, e: IOException) {
                    Log.e("JUSTIN", "Error: ${e.message}")
                }
            })
        } catch (e: Exception) {
            Log.e("JUSTIN", "Error: ${e.message}")
        }

        return promise
    }
    //Sets professor Location in database hitting our API - Justin
    fun setProfessorMarker(coordinates:LatLng){
        val json = """
            {
                "coordinates": {
                "lat": "${coordinates.latitude}",
                "long": "${coordinates.longitude}"
                }
            }"""
        val token = getUserToken()

        try {
            val url = "http://10.0.2.2:8008/account/setProfessorLocation"
            val request = Request.Builder()
                .url(url)
                .put(json.toRequestBody("application/json".toMediaType()))
                .header("Authorization", "Bearer $token")
                .build()
            Client.newCall(request).enqueue(object : Callback {
                override fun onResponse(call: Call, response: Response) {

                }
                override fun onFailure(call: Call, e: IOException) {
                    Log.e("JUSTIN", "Error: ${e.message}")
                }
            })
        } catch (e: Exception) {
            Log.e("JUSTIN", "Error: ${e.message}")
        }

    }


    //Grabs professor data - Justin
    fun getProfessorData(): CompletableFuture<Pair<String, LatLng>> {
        val token = getUserToken()
        val url = "http://10.0.2.2:8008/account/getProfessor"
        val promise = CompletableFuture<Pair<String, LatLng>>()
        val request = Request.Builder()
            .url(url)
            .get()
            .header("Authorization", "Bearer $token")
            .build()

        Client.newCall(request).enqueue(object : Callback {
            override fun onResponse(call: Call, response: Response) {
                var responseBody = response.body!!.string()
                val gson = Gson()
                val profData = gson.fromJson(StringReader(responseBody), ProfessorData::class.java)
                val coord = LatLng(profData.lat, profData.long)
                val res = Pair(first = profData.profilepic, second = coord)
                if (!response.isSuccessful) {
                    promise.complete(res)
                } else {
                    promise.complete(res)
                    Log.d("JUSTIN", responseBody)
                }
            }

            override fun onFailure(call: Call, e: IOException) {
                Log.e("JUSTIN", "Error: ${e.message}")
            }
        })
        return promise
    }



        fun getChatRoomMessages(room: String): CompletableFuture<List<ChatMessage>> {
            val promise = CompletableFuture<List<ChatMessage>>()
            try {
                val url = "http://10.0.2.2:8008/chatroom/messages/$room"
                Log.d("GET MESSAGES", url)
                val request = Request.Builder()
                    .url(url)
                    .build()

                Client.newCall(request).enqueue(object : Callback {
                    override fun onResponse(call: Call, response: Response) {
                        Log.d("GET MESSAGES", "In Response")
                        if (response.isSuccessful) {
                            Log.d("GET MESSAGES", "Sucesss")
                            val responseData = response.body?.string()
                            val jsonObject = JSONObject(responseData)
                            val jsonMessages = jsonObject.getJSONArray("messages")
                            val messages = mutableListOf<ChatMessage>()
                            Log.d("GET MESSAGES", "Processing")
                            for (i in 0 until jsonMessages.length()) {
                                val jsonMessage = jsonMessages.getJSONObject(i)
                                val gson = Gson()
                                val message = gson.fromJson(
                                    StringReader(jsonMessage.toString()),
                                    ChatMessage::class.java
                                )
                                // Parse other properties of ChatMessage if necessary
                                messages.add(message)

                            }
                            promise.complete(messages)
                        } else {
                            promise.completeExceptionally(Exception("Failed to fetch messages. Response code: ${response.code}"))
                        }
                    }

                    override fun onFailure(call: Call, e: IOException) {
                        Log.e("JUSTIN", "Error: ${e.message}")
                    }
                })
            } catch (e: Exception) {
                Log.e("JUSTIN", "Error: ${e.message}")
            }

            return promise
        }
}



