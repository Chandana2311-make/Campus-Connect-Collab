package com.example.campusconnectandcollab.ui.screens

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.example.campusconnectandcollab.ui.navigation.AppRoute

@Composable
fun AdminEventsScreen(navController: NavController) {

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
    ) {

        Text(
            text = "Event Management",
            fontSize = 26.sp,
            fontWeight = FontWeight.Bold
        )

        Spacer(modifier = Modifier.height(20.dp))

        // Add Event Button
        Button(
            onClick = { navController.navigate(AppRoute.AddEvent.route) },
            modifier = Modifier.fillMaxWidth()
        ) {
            Text(text = "Add New Event")
        }

        Spacer(modifier = Modifier.height(20.dp))

        Text("Event List:", fontSize = 20.sp, fontWeight = FontWeight.SemiBold)

        Spacer(modifier = Modifier.height(10.dp))

        // UI-only sample events
        val sampleEvents = listOf("Hackathon 2025", "Tech Talk", "Art Fest")

        sampleEvents.forEach { event ->
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 6.dp)
                    .clickable {
                        navController.navigate(AppRoute.EventDetail.createRoute(event))
                    },
                elevation = CardDefaults.cardElevation(4.dp)
            ) {
                Column(modifier = Modifier.padding(16.dp)) {
                    Text(
                        text = event,
                        fontSize = 18.sp,
                        fontWeight = FontWeight.Medium
                    )
                }
            }
        }
    }
}
