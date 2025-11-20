package com.example.campusconnectandcollab.ui.screens

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import com.example.campusconnectandcollab.ui.models.Event

@Composable
fun AdminDashboardScreen(navController: NavHostController) {

    // Sample mutable list of events
    var events by remember {
        mutableStateOf(
            mutableListOf(
                Event(
                    id = "1",
                    name = "Hackathon 2025",
                    title = "Hackathon 2025",
                    description = "A coding competition for students across the campus.",
                    date = "Dec 5 · 10:00 AM",
                    venue = "Auditorium A",
                    imageUrl = "https://yourimageurl.com/hackathon.jpg"
                ),
                Event(
                    id = "2",
                    name = "RoboRace Championship",
                    title = "RoboRace Championship",
                    description = "Robot racing competition for all engineering students.",
                    date = "Dec 12 · 11:00 AM",
                    venue = "Mechanical Block",
                    imageUrl = "https://yourimageurl.com/roborace.jpg"
                ),
                Event(
                    id = "3",
                    name = "Coding Contest",
                    title = "Coding Contest",
                    description = "Competitive programming contest for CSE students.",
                    date = "Jan 3 · 9:00 AM",
                    venue = "CSE Lab",
                    imageUrl = "https://yourimageurl.com/codingcontest.jpg"
                )
            )
        )
    }

    var showAddDialog by remember { mutableStateOf(false) }
    var editEvent by remember { mutableStateOf<Event?>(null) }

    Scaffold(
        floatingActionButton = {
            FloatingActionButton(onClick = { editEvent = null; showAddDialog = true }) {
                Icon(imageVector = Icons.Default.Add, contentDescription = "Add Event")
            }
        }
    ) { paddingValues ->

        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .padding(16.dp)
        ) {
            Text(
                text = "Admin Dashboard",
                style = MaterialTheme.typography.titleLarge
            )

            Spacer(modifier = Modifier.height(16.dp))

            LazyColumn(
                verticalArrangement = Arrangement.spacedBy(12.dp),
                modifier = Modifier.fillMaxSize()
            ) {
                items(events) { event ->
                    AdminEventCard(
                        event = event,
                        onEdit = { editEvent = it; showAddDialog = true },
                        onDelete = {
                            events = events.filter { e -> e.id != it.id }.toMutableList()
                        }
                    )
                }
            }
        }

        // Add/Edit dialog
        if (showAddDialog) {
            AddEditEventDialog(
                event = editEvent,
                onDismiss = { showAddDialog = false },
                onSave = { newEvent ->
                    events = if (editEvent != null) {
                        events.map { if (it.id == newEvent.id) newEvent else it }.toMutableList()
                    } else {
                        (events + newEvent).toMutableList()
                    }
                    showAddDialog = false
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
    var expanded by remember { mutableStateOf(false) }

    Card(
        modifier = Modifier.fillMaxWidth(),
        elevation = CardDefaults.cardElevation(8.dp)
    ) {
        Column(modifier = Modifier.padding(16.dp)) {

            // Title
            Text(text = event.title, style = MaterialTheme.typography.titleMedium)

            // Date & Venue
            Text(text = event.date, style = MaterialTheme.typography.bodyMedium)
            Text(text = event.venue, style = MaterialTheme.typography.bodyMedium)

            Spacer(modifier = Modifier.height(8.dp))

            // Description (collapsible)
            Text(
                text = event.description,
                maxLines = if (expanded) Int.MAX_VALUE else 1,
                overflow = TextOverflow.Ellipsis,
                style = MaterialTheme.typography.bodySmall
            )

            TextButton(
                onClick = { expanded = !expanded },
            ) {
                Text(if (expanded) "Show Less" else "Show More")
            }

            Spacer(modifier = Modifier.height(8.dp))

            // Buttons
            Row(horizontalArrangement = Arrangement.spacedBy(16.dp)) {
                Button(onClick = { onEdit(event) }) { Text("Edit") }
                Button(onClick = { onDelete(event) }) { Text("Delete") }
            }
        }
    }
}
