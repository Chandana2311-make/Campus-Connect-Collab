package com.example.campusconnectandcollab.ui.screens

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Refresh
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import com.example.campusconnectandcollab.ui.models.Event
import com.example.campusconnectandcollab.ui.viewmodels.EventViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun StudentDashboardScreen(
    navController: NavHostController,
    eventViewModel: EventViewModel
) {
    val events by eventViewModel.events.collectAsState()

    Scaffold(topBar = {
        TopAppBar(
            title = { Text("Upcoming Events") },
            navigationIcon = {
                IconButton(onClick = { navController.navigate("login") { popUpTo("login") { inclusive = true } } }) {
                    Icon(Icons.Default.ArrowBack, contentDescription = "Back")
                }
            },
            actions = {
                // FIX: This now calls the correct fetchEvents function
                IconButton(onClick = { eventViewModel.fetchEvents() }) {
                    Icon(Icons.Default.Refresh, contentDescription = "Refresh")
                }
            }
        )
    }) { paddingValues ->
        Column(modifier = Modifier.fillMaxSize().padding(paddingValues).padding(16.dp)) {
            Spacer(Modifier.height(8.dp))
            LazyColumn(verticalArrangement = Arrangement.spacedBy(12.dp)) {
                items(events) { event ->
                    StudentEventCard(event = event, onJoinClick = {
                        // TODO: Implement join logic here in the future
                    })
                }
            }
        }
    }
}

@Composable
fun StudentEventCard(event: Event, onJoinClick: () -> Unit) {
    val isFull = event.registeredCount >= event.totalSlots && event.totalSlots > 0

    Card(modifier = Modifier.fillMaxWidth(), elevation = CardDefaults.cardElevation(4.dp)) {
        Column(Modifier.padding(16.dp)) {
            // FIX: Using the correct field names from the Event data class
            Text(event.eventName, style = MaterialTheme.typography.titleLarge, fontWeight = FontWeight.Bold)
            Spacer(Modifier.height(4.dp))
            Text("Date: ${event.eventDate}", style = MaterialTheme.typography.bodyMedium)
            Spacer(Modifier.height(8.dp))
            Text(event.eventDescription, style = MaterialTheme.typography.bodyLarge)
            Spacer(Modifier.height(16.dp))

            LinearProgressIndicator(
                progress = { if (event.totalSlots > 0) event.registeredCount.toFloat() / event.totalSlots.toFloat() else 0f },
                modifier = Modifier.fillMaxWidth()
            )
            Spacer(Modifier.height(8.dp))
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = androidx.compose.ui.Alignment.CenterVertically
            ) {
                Text(
                    "Slots: ${event.registeredCount} / ${event.totalSlots}",
                    style = MaterialTheme.typography.bodyMedium,
                    fontWeight = FontWeight.SemiBold
                )
                Button(onClick = onJoinClick, enabled = !isFull) {
                    Text(if (isFull) "Full" else "Join")
                }
            }
        }
    }
}
