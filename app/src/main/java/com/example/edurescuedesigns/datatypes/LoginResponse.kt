package com.example.edurescuedesigns.datatypes

data class LoginResponse(
    val emailError : Boolean,
    val passwordError : Boolean,
    val token : String,
    val message : String
)
