package com.example.campusconnectandcollab.ui.screen

import android.widget.Toast
import androidx.compose.foundation.clickable
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
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.example.campusconnectandcollab.ui.models.Event
import com.example.campusconnectandcollab.ui.viewmodels.EventViewModel
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun StudentEventsScreen(
    navController: NavController,
    eventViewModel: EventViewModel
) {
    val context = LocalContext.current

    val events by eventViewModel.events.collectAsState()
    val isLoading by eventViewModel.isLoading.collectAsState()

    LaunchedEffect(Unit) {
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
            when {
                isLoading -> CircularProgressIndicator()

                events.isEmpty() -> Text(text = "No upcoming events at the moment.")

                else -> {
                    LazyColumn(
                        modifier = Modifier.fillMaxSize(),
                        contentPadding = PaddingValues(16.dp),
                        verticalArrangement = Arrangement.spacedBy(14.dp)
                    ) {
                        items(items = events, key = { it.id }) { event ->
                            StudentEventCard(
                                event = event,
                                onOpenDetails = {
                                    if (event.id.isNotBlank()) {
                                        navController.navigate("event_detail/${event.id}")
                                    } else {
                                        Toast.makeText(context, "Event ID missing", Toast.LENGTH_SHORT).show()
                                    }
                                },
                                onRegister = {
                                    if (event.registeredCount >= event.totalSlots) {
                                        Toast.makeText(context, "Event is full!", Toast.LENGTH_SHORT).show()
                                    } else {
                                        eventViewModel.registerForEvent(event.id)
                                        Toast.makeText(context, "Registered!", Toast.LENGTH_SHORT).show()
                                    }
                                }
                            )
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun StudentEventCard(
    event: Event,
    onOpenDetails: () -> Unit,
    onRegister: () -> Unit
) {
    val dateText = rememberEventDateText(event)

    Card(
        modifier = Modifier
            .fillMaxWidth()
            .clickable { onOpenDetails() }, // ✅ whole card clickable
        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface)
    ) {
        Column(
            modifier = Modifier.padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            Text(
                text = event.eventName,
                style = MaterialTheme.typography.titleLarge,
                fontWeight = FontWeight.Bold
            )

            // Description: show short preview (cleaner)
            Text(
                text = event.eventDescription,
                style = MaterialTheme.typography.bodyMedium,
                maxLines = 2,
                overflow = TextOverflow.Ellipsis,
                color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.8f)
            )

            // Date + location line
            Text(
                text = "${dateText} • ${event.location.ifBlank { "Venue TBA" }}",
                style = MaterialTheme.typography.bodySmall,
                color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.75f)
            )

            // Slots
            Text(
                text = "Slots: ${event.registeredCount} / ${event.totalSlots}",
                style = MaterialTheme.typography.bodyMedium,
                fontWeight = FontWeight.SemiBold
            )

            Spacer(Modifier.height(6.dp))

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.End
            ) {
                Button(
                    onClick = onRegister,
                    enabled = event.registeredCount < event.totalSlots
                ) {
                    Text(text = if (event.registeredCount < event.totalSlots) "Register" else "Full")
                }
            }
        }
    }
}

/**
 * ✅ Formats Firestore Timestamp nicely.
 * - If date is null (still being written), shows "Date pending"
 */
@Composable
private fun rememberEventDateText(event: Event): String {
    val ts = event.eventDate
    return if (ts == null) {
        "Date pending"
    } else {
        val date: Date = ts.toDate()
        val sdf = SimpleDateFormat("dd MMM yyyy, hh:mm a", Locale.getDefault())
        sdf.format(date)
    }
}
