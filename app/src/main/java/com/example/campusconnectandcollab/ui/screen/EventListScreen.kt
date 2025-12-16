package com.example.campusconnectandcollab.ui.screen

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.example.campusconnectandcollab.ui.models.Event // Make sure this import is present
import com.example.campusconnectandcollab.ui.viewmodels.EventViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun EventListScreen(
    navController: NavController,
    eventViewModel: EventViewModel
) {
    val events by eventViewModel.events.collectAsState()
    val isLoading by eventViewModel.isLoading.collectAsState()

    LaunchedEffect(key1 = Unit) {
        eventViewModel.fetchEvents()
    }

    Scaffold(
        topBar = {
            // --- THIS WAS THE FINAL ERROR ---
            // We need to be explicit with all parameters for TopAppBar as well.
            TopAppBar(
                title = { Text(text = "Admin Dashboard") },
                navigationIcon = {},
                actions = {}
            )
        },
        floatingActionButton = {
            FloatingActionButton(onClick = { navController.navigate("add_event") }) {
                Icon(Icons.Default.Add, contentDescription = "Add Event")
            }
        }
    ) { paddingValues ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues),
            contentAlignment = Alignment.Center
        ) {
            if (isLoading) {
                CircularProgressIndicator()
            } else if (events.isEmpty()) {
                Text(text = "No events found. Press '+' to add an event.")
            } else {
                LazyColumn(
                    modifier = Modifier.fillMaxSize(),
                    contentPadding = PaddingValues(16.dp),
                    verticalArrangement = Arrangement.spacedBy(16.dp)
                ) {
                    items(items = events, key = { it.id }) { event ->
                        AdminEventCard(
                            event = event,
                            onEdit = {
                                navController.navigate("add_event")
                            },
                            onDelete = {
                                eventViewModel.deleteEvent(event.id)
                            }
                        )
                    }
                }
            }
        }
    }
}

@Composable
fun AdminEventCard(event: Event, onEdit: () -> Unit, onDelete: () -> Unit) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
    ) {
        Column(
            modifier = Modifier.padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            Text(text = event.eventName, style = MaterialTheme.typography.titleLarge, fontWeight = FontWeight.Bold)
            Text(text = "Description: ${event.eventDescription}", style = MaterialTheme.typography.bodyMedium)
            Text(text = "Date: ${event.eventDate}", style = MaterialTheme.typography.bodyMedium)
            Text(text = "Available Slots: ${event.registeredCount} / ${event.totalSlots}", style = MaterialTheme.typography.bodyMedium)
            Text(text = "Registration Link: ${event.formLink}", style = MaterialTheme.typography.bodySmall, maxLines = 1)

            Spacer(Modifier.height(8.dp))

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.End,
                verticalAlignment = Alignment.CenterVertically
            ) {
                IconButton(onClick = onEdit) {
                    Icon(Icons.Default.Edit, contentDescription = "Edit")
                }
                IconButton(onClick = onDelete) {
                    Icon(Icons.Default.Delete, contentDescription = "Delete", tint = MaterialTheme.colorScheme.error)
                }
            }
        }
    }
}
