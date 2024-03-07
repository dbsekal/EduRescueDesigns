import android.icu.text.SimpleDateFormat
import android.util.Log
import androidx.compose.foundation.layout.*
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
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



@Composable
fun ChatRoomScreen(socketManager: SocketManager = SocketManager.getInstance(), navController: NavController) {

    var chatMessages by remember { mutableStateOf(listOf<ChatMessage>()) }
    var newMessage by remember { mutableStateOf("") }

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

                    Network().getChatRoomMessages(user.enrollment).thenAccept(){
                            messages ->
                        if(messages.isNotEmpty()){
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

/*TODO @Katelynn
   1. Design this page
   2. Take the user variable and make the messages display their name
   3. Add a hover effect on the user's name to display email
   Note: User data class can be found at User.kt. You need to login to intialize the user (will fix)
 */



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
                Text("${chatMessage.sender} : ${chatMessage.message} (${formatTime(chatMessage.timestamp)})")
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
                    socketManager.sendMessage(newMessage,user)
                    newMessage = ""
                }
            },
            modifier = Modifier.fillMaxWidth()
        ) {
            Text("Send")
        }
    }
}


fun formatTime(timestamp: Long): String {
    val sdf = SimpleDateFormat("hh:mm a", Locale.US)
    return try {
        val netDate = Date(timestamp)
        sdf.format(netDate)
    } catch (e: Exception) {
        e.toString()
    }
}
