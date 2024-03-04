package com.example.edurescuedesigns

import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import com.example.edurescuedesigns.datatypes.LoginResponse
import com.example.edurescuedesigns.datatypes.User
import com.google.gson.Gson
import okhttp3.Call
import okhttp3.Callback
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.RequestBody.Companion.toRequestBody
import okhttp3.Response
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


    fun setToken(token:String){
        val editor = sharedpref.edit()
        editor.putString("token", token).apply()
    }
    fun getToken():String{
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
    fun getUserInfo(): CompletableFuture<User> {
        val promise = CompletableFuture<User>()

        val token = getToken()

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


}