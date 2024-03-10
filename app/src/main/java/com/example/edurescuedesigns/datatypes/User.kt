package com.example.edurescuedesigns.datatypes

data class User(
    val firstName : String = "",
    val lastName : String = "",
    val email : String = "",
    val id: Int = -1,
    val enrollment: String = "",
    val phone: String = "",
    val profilepic: String = "",
    val validToken: Boolean = false,
    val type: String = "student"
)
