
import android.icu.text.SimpleDateFormat
import android.util.Log
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.Send
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import coil.compose.AsyncImage
import com.example.edurescuedesigns.classes.Network
import com.example.edurescuedesigns.classes.SocketManager
import com.example.edurescuedesigns.datatypes.ChatMessage
import com.example.edurescuedesigns.datatypes.User
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import java.util.Date
import java.util.Locale


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

    val screenHeight = LocalConfiguration.current.screenHeightDp.dp
    val spacerHeight = (screenHeight * 0.70f).value.toInt()

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
    ) {
        Box(modifier = Modifier
            .fillMaxWidth()
            .weight(1f),
            contentAlignment = Alignment.TopStart
//            .padding(16.dp)
//            .verticalScroll(state = scrollState)
//            .height(spacerHeight.dp)
//            .background(color = Color.Blue)
        ) {
            // Display chat messages
            Column(
                modifier = Modifier
                    .padding(16.dp)
                    .verticalScroll(rememberScrollState())
//                    .background(color = Color.Blue)
            ) {
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
                    }
                }
                if (showEmailBox) {
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(16.dp)
                            .background(Color.Blue)
                            .clickable {
                                // Hide the email box when clicked
                                showEmailBox = false
                            },
                        contentAlignment = Alignment.Center
                    ) {
                        Text("Email: $clickedUserEmail")
                    }
                }

            }

        }
        Box{
            // Input field for sending message
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .wrapContentHeight(),
            ) {
                TextField(
                    value = newMessage,
                    onValueChange = { newMessage = it },
                    modifier = Modifier.padding(start=10.dp, end=16.dp),
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
        Spacer(modifier = Modifier.weight(0.12f))

    }
}