package com.example.campusconnectandcollab.ui.screens

import android.content.Intent
import android.net.Uri
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import com.example.campusconnectandcollab.ui.viewmodels.EventViewModel

@Composable
fun StudentDashboardScreen(
    navController: NavHostController? = null,
    eventViewModel: EventViewModel = viewModel(),
    onEventsClick: () -> Unit
) {
    val events by eventViewModel.events.collectAsState()
    val context = LocalContext.current

    val googleFormLink =
        "https://docs.google.com/forms/d/e/1FAIpQLSfnPbbC1PkPrk-czA0hdtaJsdz4nuH0Q-Hz1cb49Zq7x2FJFw/viewform?usp=sf_link"

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
    ) {

        Text(
            text = "Upcoming Events",
            style = MaterialTheme.typography.headlineSmall
        )

        Spacer(modifier = Modifier.height(16.dp))

        LazyColumn(verticalArrangement = Arrangement.spacedBy(12.dp)) {
            items(events) { event ->
                Card(
                    modifier = Modifier.fillMaxWidth(),
                    elevation = CardDefaults.cardElevation(6.dp)
                ) {
                    Column(Modifier.padding(16.dp)) {

                        Text(event.title, style = MaterialTheme.typography.titleMedium)
                        Spacer(Modifier.height(6.dp))

                        Text(event.description)
                        Spacer(Modifier.height(6.dp))

                        Text("Date: ${event.date}")
                        Text("Venue: ${event.venue}")
                        Spacer(Modifier.height(12.dp))

                        Button(
                            onClick = {
                                val intent = Intent(Intent.ACTION_VIEW, Uri.parse(googleFormLink))
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
