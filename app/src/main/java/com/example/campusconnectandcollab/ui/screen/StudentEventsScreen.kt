package com.example.campusconnectandcollab.ui.screens

import android.content.Intent
import android.net.Uri
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import com.example.campusconnectandcollab.ui.viewmodels.EventViewModel

// TEMP DATA MODEL (replace later with your real Event model)
data class Event(
    val id: String,
    val title: String,
    val description: String,
    val date: String,
    val formLink: String   // Google Form Link
)

@Composable
fun StudentEventsScreen(navController: NavHostController, eventViewModel: EventViewModel) {

    val context = LocalContext.current

    // Updated with your real Google Form link
    val googleFormLink =
        "https://forms.gle/jArVLsVeyMTWjvnQA"

    // TODO: Replace with real events from backend
    val sampleEvents = listOf(
        Event(
            id = "1",
            title = "Campus Hackathon",
            description = "24-hour coding challenge organized by the admin team.",
            date = "Dec 15, 2025",
            formLink = googleFormLink
        ),
        Event(
            id = "2",
            title = "EV Innovation Workshop",
            description = "Learn fundamentals of Electric Vehicles and battery tech.",
            date = "Dec 20, 2025",
            formLink = googleFormLink
        )
    )

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
    ) {

        Text(
            text = "Upcoming Events",
            style = MaterialTheme.typography.headlineSmall
        )

        Spacer(modifier = Modifier.height(12.dp))

        LazyColumn(
            modifier = Modifier.fillMaxSize(),
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {

            items(sampleEvents) { event ->

                Card(
                    modifier = Modifier.fillMaxWidth(),
                    elevation = CardDefaults.cardElevation(4.dp)
                ) {
                    Column(modifier = Modifier.padding(16.dp)) {

                        Text(text = event.title, style = MaterialTheme.typography.titleMedium)
                        Spacer(modifier = Modifier.height(6.dp))

                        Text(text = event.description)
                        Spacer(modifier = Modifier.height(6.dp))

                        Text(text = "Date: ${event.date}")
                        Spacer(modifier = Modifier.height(12.dp))

                        // JOIN BUTTON → OPENS GOOGLE FORM LINK
                        Button(
                            onClick = {
                                val intent = Intent(Intent.ACTION_VIEW, Uri.parse(event.formLink))
                                context.startActivity(intent)
                            }
                        ) {
                            Text("Join")
                        }
                    }
                }
            }
        }
    }
}
