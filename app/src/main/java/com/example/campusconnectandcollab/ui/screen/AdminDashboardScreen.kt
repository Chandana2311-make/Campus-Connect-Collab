package com.example.campusconnectandcollab.ui.screens

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
// FIX: The broken import statement has been separated into two correct lines
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Refresh
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.runtime.getValue
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import com.example.campusconnectandcollab.ui.models.Event
import com.example.campusconnectandcollab.ui.screen.AddEditEventDialog
import com.example.campusconnectandcollab.ui.viewmodels.EventViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AdminDashboardScreen(
    navController: NavHostController,
    eventViewModel: EventViewModel
) {
    val events by eventViewModel.events.collectAsState()

    var showDialog by remember { mutableStateOf(false) }
    var eventToEdit by remember { mutableStateOf<Event?>(null) }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Admin Dashboard") },
                navigationIcon = {
                    IconButton(onClick = { navController.navigate("login") { popUpTo("login") { inclusive = true } } }) {
                        Icon(Icons.Default.ArrowBack, contentDescription = "Back")
                    }
                },
                actions = {
                    IconButton(onClick = { eventViewModel.fetchEvents() }) {
                        Icon(Icons.Default.Refresh, contentDescription = "Refresh")
                    }
                }
            )
        },
        floatingActionButton = {
            FloatingActionButton(onClick = { eventToEdit = null; showDialog = true }) {
                Icon(Icons.Default.Add, contentDescription = "Add Event")
            }
        }
    ) { paddingValues ->
        Column(modifier = Modifier
            .fillMaxSize()
            .padding(paddingValues)
            .padding(16.dp)) {
            LazyColumn(verticalArrangement = Arrangement.spacedBy(12.dp)) {
                items(events) { event ->
                    AdminEventEventCard(
                        event = event,
                        onEdit = { eventToEdit = it; showDialog = true },
                        onDelete = { eventViewModel.deleteEvent(event.id) }
                    )
                }
            }
        }

        if (showDialog) {
            AddEditEventDialog(
                event = eventToEdit,
                onDismiss = { showDialog = false },
                onSave = { event ->
                    if (eventToEdit == null) {
                        eventViewModel.addEvent(event)
                    } else {
                        eventViewModel.updateEvent(event)
                    }
                    showDialog = false
                }
            )
        }
    }
}

@Composable
fun AdminEventEventCard(event: Event, onEdit: (Event) -> Unit, onDelete: () -> Unit) {
    Card(modifier = Modifier.fillMaxWidth(), elevation = CardDefaults.cardElevation(4.dp)) {
        Column(Modifier.padding(16.dp)) {
            Text(event.eventName, style = MaterialTheme.typography.titleLarge, fontWeight = FontWeight.Bold)
            Spacer(Modifier.height(4.dp))
            Text("Date: ${event.eventDate}", style = MaterialTheme.typography.bodyMedium)
            Text("Slots: ${event.registeredCount} / ${event.totalSlots}", style = MaterialTheme.typography.bodyMedium)
            Spacer(Modifier.height(8.dp))
            Text(event.eventDescription, style = MaterialTheme.typography.bodyLarge)
            Spacer(Modifier.height(16.dp))
            Row(horizontalArrangement = Arrangement.spacedBy(12.dp)) {
                Button(onClick = { onEdit(event) }) { Text("Edit") }
                Button(onClick = { onDelete() }, colors = ButtonDefaults.buttonColors(containerColor = MaterialTheme.colorScheme.error)) { Text("Delete") }
            }
        }
    }
}
