import android.util.Log
import androidx.compose.foundation.layout.*
import androidx.compose.material.*
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.example.edurescuedesigns.ChatMessage
import com.example.edurescuedesigns.LoginResponse
import com.example.edurescuedesigns.Network
import com.example.edurescuedesigns.SocketManager
import com.example.edurescuedesigns.User
import com.google.gson.Gson
import io.socket.client.Socket
import org.json.JSONObject



@Composable
fun ChatRoomScreen(socketManager: SocketManager = SocketManager.getInstance()) {
    var user by remember { mutableStateOf(User()) }

    //Ensures that the fetch request is only done iff user needs validation
    LaunchedEffect(Unit) {
        if (!user.validToken) {
            // Fetch user info only if the user token is not valid
            Network().getUserInfo().thenAccept { response ->
                Log.d("SERVER RES:", response)
                val gson = Gson()
                val loginResponse = gson.fromJson(response, User::class.java)

                if (loginResponse.validToken) {
                    Log.d("Token RES", "valid")
                    user = loginResponse
                } else {
                    Log.d("Token RES", "invalid")
                }
            }
        }
    }

/*TODO @Katelynn
   1. Design this page
   2. Take the user variable and make the messages display their name
   3. Add a hover effect on the user's name to display email
   Note: User data class can be found at User.kt. You need to login to intialize the user (will fix)
 */

    var chatMessages by remember { mutableStateOf(listOf<ChatMessage>()) }
    var newMessage by remember { mutableStateOf("") }

    //We listen for messages vie flow. Find the Flow in SocketManager.kt
    LaunchedEffect(Unit) {
        socketManager.messageFlow.collect { chatMessage ->
            chatMessages = chatMessages + chatMessage
        }
    }

    Column(modifier = Modifier.padding(16.dp)) {
        // Display chat messages
        Column {
            for (chatMessage in chatMessages) {
                Text("${chatMessage.sender}: ${chatMessage.message} (${chatMessage.timestamp})")
            }
        }

        // Input field for sending messages
        TextField(
            value = newMessage,
            onValueChange = { newMessage = it },
            modifier = Modifier.padding(vertical = 16.dp),
            label = { Text("Message") },
            singleLine = true,
        )

        // Button to send messages
        Button(
            onClick = {
                if (newMessage.isNotEmpty()) {
                    socketManager.sendMessage(newMessage)
                    newMessage = ""
                }
            },
            modifier = Modifier.fillMaxWidth()
        ) {
            Text("Send")
        }
    }
}
