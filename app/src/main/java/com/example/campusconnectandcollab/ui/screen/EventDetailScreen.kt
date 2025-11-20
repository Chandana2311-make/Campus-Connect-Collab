package com.example.campusconnectandcollab.ui.screens

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.foundation.clickable
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController

@Composable
fun EventDetailScreen(
    navController: NavController,
    eventId: String
) {

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(
                Brush.verticalGradient(
                    listOf(Color(0xFFEEF1FF), Color(0xFFDDE7FF))
                )
            )
            .padding(16.dp)
    ) {

        // Back button
        Text(
            text = "< Back",
            fontSize = 18.sp,
            color = MaterialTheme.colorScheme.primary,
            modifier = Modifier
                .clickable { navController.popBackStack() }
                .padding(bottom = 16.dp)
        )

        Card(
            shape = RoundedCornerShape(16.dp),
            elevation = CardDefaults.cardElevation(10.dp),
            modifier = Modifier.fillMaxWidth()
        ) {
            Column(modifier = Modifier.padding(20.dp)) {

                Text(
                    text = "Event #$eventId",
                    fontSize = 22.sp,
                    fontWeight = FontWeight.Bold
                )

                Spacer(modifier = Modifier.height(12.dp))

                Text(
                    text = "Event Title: Tech Talk on AI Innovations",
                    fontSize = 18.sp,
                    fontWeight = FontWeight.SemiBold
                )

                Spacer(modifier = Modifier.height(8.dp))

                Text(
                    text = "Date: November 25, 2025",
                    fontSize = 16.sp
                )

                Spacer(modifier = Modifier.height(8.dp))

                Text(
                    text = "Time: 10:00 AM - 12:00 PM",
                    fontSize = 16.sp
                )

                Spacer(modifier = Modifier.height(8.dp))

                Text(
                    text = "Venue: DSATM Seminar Hall",
                    fontSize = 16.sp
                )

                Spacer(modifier = Modifier.height(16.dp))

                Text(
                    text = "Description:",
                    fontSize = 18.sp,
                    fontWeight = FontWeight.SemiBold
                )

                Spacer(modifier = Modifier.height(6.dp))

                Text(
                    text = "Join us for a deep dive into cutting-edge AI research, real industrial applications, and future careers in machine intelligence.",
                    fontSize = 15.sp,
                    lineHeight = 22.sp
                )
            }
        }
    }
}
