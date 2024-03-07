package com.example.edurescuedesigns.datatypes

data class RegisterResponse(
    val emailError : Boolean, // if email doesn't alr have an acct
    val passwordError : Boolean, // password doesn't pass regex
    val message : String // notes on response

)
