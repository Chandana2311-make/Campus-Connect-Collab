package com.example.campusconnectandcollab.ui.screen

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.example.campusconnectandcollab.ui.models.Event
import com.example.campusconnectandcollab.ui.viewmodels.EventViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun StudentEventsScreen(
    navController: NavController,
    eventViewModel: EventViewModel
) {
    // Collect state from the shared ViewModel
    val events by eventViewModel.events.collectAsState()
    val isLoading by eventViewModel.isLoading.collectAsState()

    // **IMPORTANT**: This ensures data is fetched when the screen appears,
    // even if it was already fetched on the login screen. It's a safety measure.
    LaunchedEffect(key1 = Unit) {
        eventViewModel.fetchEvents()
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text(text = "Upcoming Events") }
            )
        }
    ) { paddingValues ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues),
            contentAlignment = Alignment.Center
        ) {
            // Show a loading spinner while data is being fetched
            if (isLoading) {
                CircularProgressIndicator()
            }
            // If loading is done and the list is empty, show a message
            else if (events.isEmpty()) {
                Text(text = "No upcoming events at the moment.")
            }
            // Once loading is done and there are events, display them
            else {
                LazyColumn(
                    modifier = Modifier.fillMaxSize(),
                    contentPadding = PaddingValues(16.dp),
                    verticalArrangement = Arrangement.spacedBy(16.dp)
                ) {
                    items(items = events, key = { it.id }) { event ->
                        // Use the special card designed for students
                        StudentEventCard(
                            event = event,
                            onRegister = {
                                eventViewModel.registerForEvent(event.id)
                            }
                        )
                    }
                }
            }
        }
    }
}

@Composable
fun StudentEventCard(event: Event, onRegister: () -> Unit) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
    ) {
        Column(
            modifier = Modifier.padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            // Display all the same event details as the admin view
            Text(text = event.eventName, style = MaterialTheme.typography.titleLarge, fontWeight = FontWeight.Bold)
            Text(text = "Description: ${event.eventDescription}", style = MaterialTheme.typography.bodyMedium)
            Text(text = "Date: ${event.eventDate}", style = MaterialTheme.typography.bodyMedium)
            Text(text = "Slots Filled: ${event.registeredCount} / ${event.totalSlots}", style = MaterialTheme.typography.bodyMedium)
            Text(text = "Registration Link: ${event.formLink}", style = MaterialTheme.typography.bodySmall, maxLines = 1)

            Spacer(Modifier.height(8.dp))

            // The only difference: A "Register" button instead of Edit/Delete
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.End
            ) {
                Button(onClick = onRegister) {
                    Text(text = "Register")
                }
            }
        }
    }
}
