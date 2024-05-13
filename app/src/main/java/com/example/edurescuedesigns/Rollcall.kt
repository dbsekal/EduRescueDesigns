package com.example.edurescuedesigns


import android.util.Log
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.Checkbox
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.example.edurescuedesigns.classes.Network
import com.example.edurescuedesigns.datatypes.RollCallStudent
import com.example.edurescuedesigns.datatypes.User
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

@Composable
fun Rollcall(navController: NavController) {
    var user by remember { mutableStateOf(User()) }
    var students by remember { mutableStateOf(listOf<RollCallStudent>()) }
    var attendance by remember { mutableStateOf(mutableMapOf<RollCallStudent, Boolean>()) }
    var submissionStatus by remember { mutableStateOf<String?>(null) }

    LaunchedEffect(Unit) {
        if (!user.validToken) {
            // Fetch user info only if the user token is not valid
            Network().getUserInfo().thenAccept { userRes ->
                if (userRes.validToken) {
                    user = userRes
                    Network().getRollCallStudents(user.enrollment).thenAccept { rollCallStudents ->
                        students = rollCallStudents
                        // Initialize attendance map with default values
                        rollCallStudents.forEach { student ->
                            attendance[student] = false
                        }
                    }
                } else {
                    CoroutineScope(Dispatchers.Main).launch {
                        navController.navigate("login")
                    }
                }
            }
        }
    }

    Column {
        // Display checkboxes for each student
        students.forEach { student ->
            Row(Modifier.padding(8.dp)) {
                Checkbox(
                    checked = attendance[student] ?: false,
                    onCheckedChange = { isChecked ->
                        attendance = attendance.toMutableMap().apply {
                            this[student] = isChecked
                        }
                    }
                )
                Text("${student.first_name} ${student.last_name}", Modifier.padding(start = 8.dp))
            }
        }

        Button(
            onClick = {
                val absentStudentsEmails = students
                    .filter { student -> !attendance[student]!! ?: false }
                    .map { student -> student.email }

                // Use the 'absentStudentsEmails' array as needed
                Log.d("Absent Students", absentStudentsEmails.toString())
                Network().notifyAbsentStudents(absentStudentsEmails)

                // Update submission status message
                submissionStatus = "Attendance has been submitted. Emails were sent to absent students."
            },
            Modifier.padding(vertical = 16.dp, horizontal = 8.dp)
        ) {
            Text("Submit Attendance")
        }

        // Display submission status message
        submissionStatus?.let { status ->
            Text(status, Modifier.padding(8.dp))
        }
    }
}




