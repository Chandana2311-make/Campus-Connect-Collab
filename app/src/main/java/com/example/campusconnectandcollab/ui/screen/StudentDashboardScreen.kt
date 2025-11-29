package com.example.campusconnectandcollab.ui.screens

import android.content.Intent
import android.net.Uri
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Refresh
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import com.example.campusconnectandcollab.ui.viewmodels.EventViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun StudentDashboardScreen(
    navController: NavHostController,
    eventViewModel: EventViewModel
) {
    val events by eventViewModel.events.collectAsState()
    val context = LocalContext.current
    val googleFormLink = "https://docs.google.com/forms/..."

    Scaffold(topBar = {
        TopAppBar(
            title = { Text("Upcoming Events") },
            navigationIcon = {
                IconButton(onClick = { navController.navigate("login") { popUpTo("login") { inclusive = true } } }) {
                    Icon(Icons.Default.ArrowBack, contentDescription = "Back")
                }
            },
            actions = {
                IconButton(onClick = { eventViewModel.refreshEvents() }) {
                    Icon(Icons.Default.Refresh, contentDescription = "Refresh")
                }
            }
        )
    }) { paddingValues ->
        Column(modifier = Modifier.fillMaxSize().padding(paddingValues).padding(16.dp)) {
            Spacer(Modifier.height(8.dp))
            LazyColumn(verticalArrangement = Arrangement.spacedBy(12.dp)) {
                items(events) { ev ->
                    Card(modifier = Modifier.fillMaxWidth(), elevation = CardDefaults.cardElevation(6.dp)) {
                        Column(Modifier.padding(16.dp)) {
                            Text(ev.title, style = MaterialTheme.typography.titleMedium)
                            Spacer(Modifier.height(6.dp))
                            Text(ev.description)
                            Spacer(Modifier.height(6.dp))
                            Text("Date: ${ev.date}")
                            Text("Venue: ${ev.venue}")
                            Spacer(Modifier.height(12.dp))
                            Button(onClick = {
                                val intent = Intent(Intent.ACTION_VIEW, Uri.parse(googleFormLink))
                                context.startActivity(intent)
                            }) { Text("Join") }
                        }
                    }
                }
            }
        }
    }
}
