package com.example.edurescuedesigns

import android.util.Log
import okhttp3.Call
import okhttp3.Callback
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.RequestBody.Companion.toRequestBody
import okhttp3.Response
import java.io.IOException
/*
error: true
passwordError: true
emailError: false

*/

class Network {
    private val MEDIA_TYPE_MARKDOWN = "application/json".toMediaType()
    private val Client = OkHttpClient()
    /*TODO - Have both the login function and register function return helpful error information
       For example: if the password is wrong, send a boolean passwordError: true so that the UI can
       prompt the user accordingly*/

    /*TODO - This may require you to change the Node.js server to return what you want*/
    fun login(email: String, password: String)  {
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
                    if (!response.isSuccessful) {
                    } else {
                        Log.d("LOGIN ATTEMPT:", response.body!!.string())
                    }
                }

                override fun onFailure(call: Call, e: IOException) {
                    Log.e("LOGIN ATTEMPT:", "Error: ${e.message}")
                }
            })
        } catch (e: Exception) {
            Log.e("LOGIN ATTEMPT:", "Error: ${e.message}")
        }
    }
    /*TODO- Create a register function that hits our API*/



}