package com.example.campusconnectandcollab.ui.screen

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.example.campusconnectandcollab.ui.models.Registration
import com.google.firebase.firestore.Query
import com.google.firebase.Firebase
import com.google.firebase.firestore.firestore

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun EventRegistrationsScreen(
    eventId: String,
    navController: NavController
) {
    val firestore = Firebase.firestore

    var regs by remember { mutableStateOf<List<Registration>>(emptyList()) }
    var loading by remember { mutableStateOf(true) }

    DisposableEffect(eventId) {
        val listener = firestore.collection("events")
            .document(eventId)
            .collection("registrations")
            .orderBy("registeredAt", Query.Direction.DESCENDING)
            .addSnapshotListener { snap, _ ->
                loading = false
                regs = snap?.documents?.mapNotNull { it.toObject(Registration::class.java) } ?: emptyList()
            }

        onDispose { listener.remove() }
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Registrations") },
                navigationIcon = {
                    IconButton(onClick = { navController.navigateUp() }) {
                        Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Back")
                    }
                }
            )
        }
    ) { padding ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding),
            contentAlignment = Alignment.Center
        ) {
            when {
                loading -> CircularProgressIndicator()
                regs.isEmpty() -> Text("No registrations yet.")
                else -> {
                    LazyColumn(
                        modifier = Modifier.fillMaxSize(),
                        contentPadding = PaddingValues(16.dp),
                        verticalArrangement = Arrangement.spacedBy(10.dp)
                    ) {
                        items(regs) { r ->
                            Card {
                                Column(modifier = Modifier.padding(14.dp)) {
                                    Text(text = r.email, style = MaterialTheme.typography.bodyLarge)
                                    Text(
                                        text = r.registeredAt?.toDate()?.toString() ?: "Just now",
                                        style = MaterialTheme.typography.bodySmall
                                    )
                                }
                            }
                        }
                    }
                }
            }
        }
    }
}
