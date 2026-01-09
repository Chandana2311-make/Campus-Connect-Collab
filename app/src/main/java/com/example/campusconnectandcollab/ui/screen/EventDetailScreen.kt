package com.example.campusconnectandcollab.ui.screen

import android.widget.Toast
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.example.campusconnectandcollab.ui.viewmodels.EventViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun EventDetailScreen(
    eventId: String,
    navController: NavController,
    eventViewModel: EventViewModel
) {
    val context = LocalContext.current

    // This assumes your ViewModel exposes a Flow/List of events. We'll adapt if yours is different.
    val events by eventViewModel.events.collectAsState(initial = emptyList())

    val event = events.firstOrNull { it.id == eventId }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Event Details") },
                navigationIcon = {
                    IconButton(onClick = { navController.navigateUp() }) {
                        Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Back")
                    }
                }
            )
        }
    ) { paddingValues ->
        if (event == null) {
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(paddingValues),
                contentAlignment = Alignment.Center
            ) {
                Text("Event not found.")
            }
            return@Scaffold
        }

        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .padding(16.dp)
                .verticalScroll(rememberScrollState()),
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            Text(event.eventName, style = MaterialTheme.typography.headlineSmall, fontWeight = FontWeight.Bold)
            Text(event.location, style = MaterialTheme.typography.bodyMedium)

            Divider()

            Text("About", style = MaterialTheme.typography.titleMedium, fontWeight = FontWeight.SemiBold)
            Text(event.eventDescription, style = MaterialTheme.typography.bodyMedium)

            Spacer(modifier = Modifier.height(6.dp))

            Text(
                text = "Slots: ${event.registeredCount} / ${event.totalSlots}",
                style = MaterialTheme.typography.bodyMedium,
                fontWeight = FontWeight.SemiBold
            )

            Spacer(modifier = Modifier.height(10.dp))

            Button(
                onClick = {
                    val isFull = event.registeredCount >= event.totalSlots
                    if (isFull) {
                        Toast.makeText(context, "Event is full!", Toast.LENGTH_SHORT).show()
                    } else {
                        // We'll implement this in ViewModel next
                        eventViewModel.registerForEvent(event.id)
                        Toast.makeText(context, "Registered successfully!", Toast.LENGTH_SHORT).show()
                    }
                },
                modifier = Modifier.fillMaxWidth(),
                enabled = event.registeredCount < event.totalSlots
            ) {
                Text(if (event.registeredCount < event.totalSlots) "Join / Register" else "Full")
            }
            OutlinedButton(
                onClick = { navController.navigate("event_regs/${event.id}") },
                modifier = Modifier.fillMaxWidth()
            ) {
                Text("View Registrations (Admin)")
            }


            OutlinedButton(
                onClick = {
                    // Optional: open Google Form link later
                    Toast.makeText(context, "Form link: ${event.formLink}", Toast.LENGTH_LONG).show()
                },
                modifier = Modifier.fillMaxWidth()
            ) {
                Text("Open Registration Form")
            }
        }
    }
}
