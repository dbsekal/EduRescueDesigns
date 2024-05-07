package com.example.edurescuedesigns

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Checkbox
import androidx.compose.material3.CheckboxDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController

//Created UI layout of attendance check -- Kayla
@Composable
fun Rollcall(navController: NavController){
    //List of student names, temp demo
    val students = listOf("Megan Chung", "Justin Fisher", "Katelynn Nguyen", "Kayla Tran", "Jane Doe", "John Doe")

    //Remembers list of mutable states, one for each student's attendance status
    val attendance = remember { mutableStateListOf(*Array(students.size) { false }) }

    Column {
        //each student will have their own checkbox
        students.forEachIndexed { index, student ->
            Row (horizontalArrangement = Arrangement.Center, verticalAlignment = Alignment.CenterVertically) {
                Checkbox(
                    checked = attendance[index], onCheckedChange = {isChecked -> attendance[index] = isChecked},
                    modifier = Modifier.padding(16.dp),
                    colors = CheckboxDefaults.colors(
                        checkedColor = Color.Red, uncheckedColor = Color.Gray
                    )
                )
                Text(student, modifier = Modifier.padding(16.dp))
            }
        }
    }

}
