package com.example.campusconnectandcollab.ui.screen

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import com.example.campusconnectandcollab.ui.models.Event // Correct import path
import com.example.campusconnectandcollab.ui.navigation.AppRoute
import com.example.campusconnectandcollab.ui.viewmodels.EventViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun EventListScreen(
    navController: NavController,
    eventViewModel: EventViewModel = viewModel(),
    isAdmin: Boolean // We pass this flag to show/hide admin controls
) {
    val events by eventViewModel.events.collectAsState()

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Upcoming Events") }
            )
        },
        floatingActionButton = {
            if (isAdmin) {
                FloatingActionButton(
                    onClick = { navController.navigate(AppRoute.AddEvent.route) },
                ) {
                    Icon(Icons.Filled.Add, contentDescription = "Add New Event")
                }
            }
        }
    ) { paddingValues ->
        if (events.isEmpty()) {
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(paddingValues),
                contentAlignment = Alignment.Center
            ) {
                Column(horizontalAlignment = Alignment.CenterHorizontally) {
                    CircularProgressIndicator()
                    Spacer(modifier = Modifier.height(16.dp))
                    Text("Fetching events...")
                }
            }
        } else {
            LazyColumn(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(paddingValues)
                    .padding(horizontal = 16.dp),
                verticalArrangement = Arrangement.spacedBy(12.dp),
                contentPadding = PaddingValues(vertical = 16.dp)
            ) {
                items(events) { event ->
                    EventCard(
                        event = event,
                        onJoinClick = { eventId ->
                            // TODO: Implement logic for joining an event using eventId
                        },
                        isAdmin = isAdmin
                    )
                }
            }
        }
    }
}

@Composable
fun EventCard(event: Event, onJoinClick: (String) -> Unit, isAdmin: Boolean) {
    // Perform calculations using Long, then convert to Float for the progress bar
    val isFull = event.registeredCount >= event.totalSlots && event.totalSlots > 0
    val progress = if (event.totalSlots > 0) {
        event.registeredCount.toFloat() / event.totalSlots.toFloat()
    } else {
        0f
    }

    Card(
        modifier = Modifier.fillMaxWidth(),
        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
    ) {
        Column(
            modifier = Modifier.padding(16.dp)
        ) {
            Text(
                text = event.eventName,
                style = MaterialTheme.typography.titleLarge,
                fontWeight = FontWeight.Bold
            )
            Spacer(modifier = Modifier.height(4.dp))
            Text(
                text = "Date: ${event.eventDate}",
                style = MaterialTheme.typography.bodyMedium,
                color = Color.Gray
            )
            Spacer(modifier = Modifier.height(12.dp))
            Text(
                text = event.eventDescription,
                style = MaterialTheme.typography.bodyLarge
            )
            Spacer(modifier = Modifier.height(16.dp))

            LinearProgressIndicator(
                progress = { progress },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(8.dp)
            )

            Spacer(modifier = Modifier.height(8.dp))

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = "Slots: ${event.registeredCount} / ${event.totalSlots}",
                    style = MaterialTheme.typography.bodyMedium,
                    fontWeight = FontWeight.SemiBold
                )

                if (!isAdmin) {
                    Button(
                        onClick = { onJoinClick(event.id) },
                        enabled = !isFull
                    ) {
                        Text(if (isFull) "Full" else "Join")
                    }
                }
            }
        }
    }
}
