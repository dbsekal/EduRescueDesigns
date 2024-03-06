package com.example.edurescuedesigns.datatypes


data class ChatMessage(
    val sender: String,
    val message: String,
    val timestamp: Long,
    val profilepic: String,
    val email: String,
)
