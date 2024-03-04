package com.example.edurescuedesigns

data class LoginResponse(
    val emailError : Boolean,
    val passwordError : Boolean,
    val token : String,
    val message : String
)
