import androidx.compose.foundation.layout.*
import androidx.compose.material.*
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.example.edurescuedesigns.ChatMessage
import com.example.edurescuedesigns.SocketManager
import com.example.edurescuedesigns.User
import io.socket.client.Socket
import org.json.JSONObject

@Composable
fun ChatRoomScreen(socketManager: SocketManager = SocketManager.getInstance()) {
    var chatMessages by remember { mutableStateOf(listOf<ChatMessage>()) }
    var newMessage by remember { mutableStateOf("") }

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
