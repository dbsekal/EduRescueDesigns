import android.icu.text.SimpleDateFormat
import android.util.Log
import coil.compose.AsyncImage
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.example.edurescuedesigns.datatypes.ChatMessage
import com.example.edurescuedesigns.classes.Network
import com.example.edurescuedesigns.classes.SocketManager
import com.example.edurescuedesigns.datatypes.User
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import java.util.Date
import java.util.Locale
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.Send
import androidx.compose.material.icons.filled.Send
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton


fun formatTime(timestamp: Long): String {
    val sdf = SimpleDateFormat("hh:mm a", Locale.US)
    return try {
        val netDate = Date(timestamp)
        sdf.format(netDate)
    } catch (e: Exception) {
        e.toString()
    }
}

@Composable
fun ChatRoomScreen(socketManager: SocketManager = SocketManager.getInstance(), navController: NavController) {

    var chatMessages by remember { mutableStateOf(listOf<ChatMessage>()) }
    var newMessage by remember { mutableStateOf("") }
    // State variable to track whether email box should be shown
    var showEmailBox by remember { mutableStateOf(false) }

    // State variable to store the email of the clicked user
    var clickedUserEmail by remember { mutableStateOf("") }
    var user by remember { mutableStateOf(User()) }
    //Ensures that the fetch request is only done iff user needs validation
    LaunchedEffect(Unit) {
        if (!user.validToken) {
            // Fetch user info only if the user token is not valid
            Network().getUserInfo().thenAccept { userRes ->
                if (userRes.validToken) {
                    Log.d("Token RES", "valid")
                    user = userRes
                    socketManager.joinRoom(user)

                    Network().getChatRoomMessages(user.enrollment).thenAccept() { messages ->
                        if (messages.isNotEmpty()) {
                            chatMessages = chatMessages + messages
                        }
                    }

                } else {
                    Log.d("Token RES", "invalid")
                    CoroutineScope(Dispatchers.Main).launch {
                        navController.navigate("login")
                    }
                }
            }


        }


    }


    //We listen for messages vie flow. Find the Flow in SocketManager.kt
    LaunchedEffect(Unit) {
        socketManager.messageFlow.collect { chatMessage ->
            chatMessages = chatMessages + chatMessage
        }
    }
    val scrollState = rememberScrollState()
    Column(
        modifier = Modifier
            .padding(16.dp)
            .fillMaxSize()
            .verticalScroll(state = scrollState)
    ) {
        // Display chat messages
            for (chatMessage in chatMessages) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    AsyncImage(
                        model = chatMessage.profilepic,
                        contentDescription = null,
                        modifier = Modifier
                            .clip(CircleShape)
                    )
                    Text(
                        text = "${chatMessage.sender} : ${chatMessage.message} (${
                            formatTime(
                                chatMessage.timestamp
                            )
                        })",
                        modifier = Modifier.clickable {
                            // Show the email box when the user's name is clicked
                            showEmailBox = true
                            clickedUserEmail = chatMessage.email
                        }
                    )
//                    Text("${chatMessage.sender} : ${chatMessage.message} (${formatTime(chatMessage.timestamp)})")
                }
            }
            if (showEmailBox) {
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(16.dp)
                        .background(Color.White)
                        .clickable {
                            // Hide the email box when clicked
                            showEmailBox = false
                        },
                    contentAlignment = Alignment.Center
                ) {
                    Text("Email: $clickedUserEmail")
                }
            }
            Spacer(modifier = Modifier.weight(1f))
            Column(
                modifier = Modifier
                    .padding(vertical = 16.dp)
            ) {
                // Input field for sending messages
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    TextField(
                        value = newMessage,
                        onValueChange = { newMessage = it },
                        modifier = Modifier.weight(1f).padding(vertical = 16.dp),
                        label = { Text("Message") },
                        singleLine = true,
                    )
                    // Button to send messages
                    IconButton(
                        onClick = {
                            if (newMessage.isNotEmpty()) {
                                socketManager.sendMessage(newMessage, user)
                                newMessage = ""
                            }
                        },
                    ) {
                        Icon(
                            imageVector = Icons.AutoMirrored.Filled.Send,
                            contentDescription = "send_message"
                        )
                    }
                }
            }

    }
}