package com.example.campusconnectandcollab.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.example.campusconnectandcollab.ui.navigation.AppRoute

@Composable
fun AdminEventListScreen(navController: NavController) {

    val dummyEvents = remember {
        mutableStateListOf(
            "Orientation Program",
            "AI Seminar",
            "Cultural Fest",
            "Sports Meet",
            "Hackathon 2025"
        )
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(
                Brush.verticalGradient(
                    listOf(Color(0xFFF8F9FF), Color(0xFFE3E7FF))
                )
            )
            .padding(16.dp)
    ) {

        // PAGE TITLE
        Text(
            text = "Admin - Manage Events",
            fontSize = 24.sp,
            fontWeight = FontWeight.Bold,
            modifier = Modifier.padding(bottom = 20.dp)
        )

        // Add Event button
        Button(
            onClick = { navController.navigate(AppRoute.AddEvent.route) },
            modifier = Modifier
                .fillMaxWidth()
                .padding(bottom = 20.dp),
            shape = RoundedCornerShape(12.dp)
        ) {
            Text("Add New Event", fontSize = 18.sp)
        }

        // Event List
        dummyEvents.forEachIndexed { index, eventName ->

            Card(
                shape = RoundedCornerShape(16.dp),
                elevation = CardDefaults.cardElevation(6.dp),
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 8.dp)
            ) {

                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(16.dp),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {

                    Column {
                        Text(eventName, fontSize = 20.sp, fontWeight = FontWeight.SemiBold)
                        Text("Event ID: ${index + 1}", fontSize = 14.sp, color = Color.DarkGray)
                    }

                    Row {
                        // Edit Button
                        Text(
                            "Edit",
                            color = MaterialTheme.colorScheme.primary,
                            fontWeight = FontWeight.Bold,
                            modifier = Modifier
                                .padding(end = 12.dp)
                                .clickable {
                                    navController.navigate(
                                        AppRoute.EditEvent.createRoute((index + 1).toString())
                                    )
                                }
                        )

                        // Delete Button
                        Text(
                            "Delete",
                            color = Color.Red,
                            fontWeight = FontWeight.Bold,
                            modifier = Modifier.clickable {
                                dummyEvents.removeAt(index)
                            }
                        )
                    }
                }
            }
        }
    }
}
