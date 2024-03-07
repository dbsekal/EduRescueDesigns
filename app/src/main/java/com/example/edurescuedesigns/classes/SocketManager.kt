package com.example.edurescuedesigns.classes

import com.example.edurescuedesigns.datatypes.ChatMessage
import com.example.edurescuedesigns.datatypes.User
import io.socket.client.IO
import io.socket.client.Socket
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.runBlocking
import org.json.JSONObject
import java.net.URISyntaxException

class SocketManager private constructor() {

    private val socket: Socket

    //Using mutable share flow to give state to messages in UI
    private val _messageFlow = MutableSharedFlow<ChatMessage>()
    val messageFlow: Flow<ChatMessage> = _messageFlow.asSharedFlow()

    init {
        //When init we create a socket with our Node.js backend
        socket = try {
            IO.socket("http://10.0.2.2:8000")
        } catch (e: URISyntaxException) {
            throw IllegalArgumentException("Invalid URI", e)
        }

        socket.connect()

        socket.on(Socket.EVENT_CONNECT) {
            // Handle connection event if needed
        }

        //When emitting a message
        socket.on("message") { args ->
            val jsonMessage = args[0] as JSONObject
            val sender = jsonMessage.optString("sender", "Unknown")
            val message = jsonMessage.getString("message")
            val profilepic = jsonMessage.getString("profilepic")
            val email = jsonMessage.getString("email")
            val timestamp = jsonMessage.getLong("timestamp")

            val chatMessage = ChatMessage(sender, message, timestamp, profilepic, email)
            runBlocking {
                _messageFlow.emit(chatMessage)
            }
        }
    }

    companion object {
        //This gives us an instance of our socket
        private var instance: SocketManager? = null

        fun getInstance(): SocketManager {
            if (instance == null) {
                instance = SocketManager()
            }
            return instance!!
        }
    }

    fun sendMessage(message: String, user: User) {
        //Emits message to broadcast to entire server
        val jsonMessage = JSONObject().apply {
            put("message", message)
            put("sender", "${user.firstName} ${user.lastName}")
            put("email", user.email)
            put("profilepic", user.profilepic)
            put("room", user.enrollment)
            put("timestamp", System.currentTimeMillis())
            // Add other data as needed
        }
        socket.emit("message", jsonMessage)
    }
    fun joinRoom(user: User) {
        val jsonMessage = JSONObject().apply {
            put("username", "${user.firstName} ${user.lastName}")
            put("room", user.enrollment)
        }
        socket.emit("joinRoom", jsonMessage)
    }

    fun disconnect() {
        socket.disconnect()
    }
}
