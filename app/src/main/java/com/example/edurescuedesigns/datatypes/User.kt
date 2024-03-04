package com.example.edurescuedesigns.datatypes

data class User(
    val firstName : String = "",
    val lastName : String = "",
    val email : String = "",
    val id: Int = -1,
    val validToken: Boolean = false
)
