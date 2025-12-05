package com.example.campusconnectandcollab.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
// REMOVED THE BAD IMPORT LINES FROM HERE
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.example.campusconnectandcollab.ui.models.Event // Correct Event model
import com.example.campusconnectandcollab.ui.navigation.AppRoute
import com.example.campusconnectandcollab.ui.viewmodels.EventViewModel // Import ViewModel

@Composable
fun AdminEventListScreen(
    navController: NavController,
    eventViewModel: EventViewModel
) {

    // --- GET REAL DATA FROM VIEWMODEL ---
    val events by eventViewModel.events.collectAsState()
    val isLoading by eventViewModel.isLoading.collectAsState()
    var eventToDelete by remember { mutableStateOf<Event?>(null) }


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

        Text(
            text = "Admin - Manage Events",
            fontSize = 24.sp,
            fontWeight = FontWeight.Bold,
            modifier = Modifier.padding(bottom = 20.dp)
        )

        Button(
            onClick = { navController.navigate(AppRoute.AddEvent.route) },
            modifier = Modifier
                .fillMaxWidth()
                .padding(bottom = 20.dp),
            shape = RoundedCornerShape(12.dp)
        ) {
            Text("Add New Event", fontSize = 18.sp)
        }

        // --- SHOW LOADER OR EVENT LIST ---
        if (isLoading) {
            Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                CircularProgressIndicator()
            }
        } else {
            LazyColumn(
                modifier = Modifier.fillMaxSize(),
                verticalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                // --- USE THE REAL 'events' LIST ---
                items(events) { event ->
                    AdminEventCard(
                        event = event,
                        onEditClick = {
                            // Navigate to Edit screen with the real event ID
                            navController.navigate(AppRoute.EditEvent.createRoute(event.id))
                        },
                        onDeleteClick = {
                            // Set the event to be deleted to show the dialog
                            eventToDelete = event
                        }
                    )
                }
            }
        }
    }

    // --- CONFIRMATION DIALOG FOR DELETE ---
    if (eventToDelete != null) {
        AlertDialog(
            onDismissRequest = { eventToDelete = null },
            title = { Text("Delete Event") },
            text = { Text("Are you sure you want to delete '${eventToDelete!!.eventName}'?") },
            confirmButton = {
                Button(
                    onClick = {
                        eventViewModel.deleteEvent(eventToDelete!!.id)
                        eventToDelete = null // Close the dialog
                    }
                ) {
                    Text("Delete")
                }
            },
            dismissButton = {
                Button(onClick = { eventToDelete = null }) {
                    Text("Cancel")
                }
            }
        )
    }
}


@Composable
fun AdminEventCard(event: Event, onEditClick: () -> Unit, onDeleteClick: () -> Unit) {
    Card(
        shape = RoundedCornerShape(16.dp),
        elevation = CardDefaults.cardElevation(6.dp),
        modifier = Modifier
            .fillMaxWidth()
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Column(modifier = Modifier.weight(1f)) {
                Text(event.eventName, fontSize = 20.sp, fontWeight = FontWeight.SemiBold)
                Text("Slots: ${event.registeredCount}/${event.totalSlots}", fontSize = 14.sp, color = Color.DarkGray)
            }

            Row {
                Text(
                    "Edit",
                    color = MaterialTheme.colorScheme.primary,
                    fontWeight = FontWeight.Bold,
                    modifier = Modifier
                        .padding(end = 16.dp)
                        .clickable(onClick = onEditClick)
                )

                Text(
                    "Delete",
                    color = Color.Red,
                    fontWeight = FontWeight.Bold,
                    modifier = Modifier.clickable(onClick = onDeleteClick)
                )
            }
        }
    }
}
