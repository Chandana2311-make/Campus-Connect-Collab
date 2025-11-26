package com.example.campusconnectandcollab.ui.screens

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import com.example.campusconnectandcollab.ui.models.Event
import com.example.campusconnectandcollab.ui.viewmodels.EventViewModel

@Composable
fun AdminDashboardScreen(
    navController: NavHostController,
    eventViewModel: EventViewModel = viewModel()
) {
    val events by eventViewModel.events.collectAsState()

    var showDialog by remember { mutableStateOf(false) }
    var eventToEdit by remember { mutableStateOf<Event?>(null) }

    Scaffold(
        floatingActionButton = {
            FloatingActionButton(
                onClick = { eventToEdit = null; showDialog = true }
            ) {
                Icon(Icons.Default.Add, contentDescription = "Add Event")
            }
        }
    ) { paddingValues ->

        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .padding(16.dp)
        ) {

            Text("Admin Dashboard", style = MaterialTheme.typography.headlineSmall)
            Spacer(Modifier.height(16.dp))

            LazyColumn(verticalArrangement = Arrangement.spacedBy(12.dp)) {
                items(events) { event ->
                    AdminEventCard(
                        event = event,
                        onEdit = {
                            eventToEdit = it
                            showDialog = true
                        },
                        onDelete = { eventViewModel.deleteEvent(it) }
                    )
                }
            }
        }

        if (showDialog) {
            AddEditEventDialog(
                event = eventToEdit,
                onDismiss = { showDialog = false },
                onSave = { e ->
                    if (eventToEdit == null) eventViewModel.addEvent(e)
                    else eventViewModel.updateEvent(e)
                    showDialog = false
                }
            )
        }
    }
}

@Composable
fun AdminEventCard(
    event: Event,
    onEdit: (Event) -> Unit,
    onDelete: (Event) -> Unit
) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        elevation = CardDefaults.cardElevation(6.dp)
    ) {
        Column(Modifier.padding(16.dp)) {

            Text(event.title, style = MaterialTheme.typography.titleMedium)
            Spacer(Modifier.height(4.dp))

            Text(event.date)
            Text(event.venue)
            Text("Max Participants: ${event.maxParticipants}")
            Spacer(Modifier.height(8.dp))

            Text(event.description)
            Spacer(Modifier.height(12.dp))

            Row(horizontalArrangement = Arrangement.spacedBy(12.dp)) {
                Button(onClick = { onEdit(event) }) { Text("Edit") }
                Button(onClick = { onDelete(event) }) { Text("Delete") }
            }
        }
    }
}
