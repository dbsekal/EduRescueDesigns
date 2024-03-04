package com.example.edurescuedesigns

data class User(
    val firstName : String = "",
    val lastName : String = "",
    val email : String = "",
    val id: Int = -1,
    val validToken: Boolean = false
)
