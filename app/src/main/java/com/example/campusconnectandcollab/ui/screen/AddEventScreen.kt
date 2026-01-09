package com.example.campusconnectandcollab.ui.screen

import android.widget.Toast
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.example.campusconnectandcollab.ui.models.Event
import com.example.campusconnectandcollab.ui.viewmodels.EventViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AddEventScreen(
    navController: NavController,
    eventViewModel: EventViewModel,
    eventId: String? = null // ✅ if null -> create, else -> edit
) {
    val context = LocalContext.current

    val events by eventViewModel.events.collectAsState()

    val existingEvent = events.firstOrNull { it.id == eventId }
    val isEditMode = existingEvent != null

    var eventName by remember { mutableStateOf("") }
    var eventDescription by remember { mutableStateOf("") }
    var location by remember { mutableStateOf("") }
    var totalSlots by remember { mutableStateOf("") }
    var formLink by remember { mutableStateOf("") }

    // ✅ Prefill fields when editing
    LaunchedEffect(existingEvent) {
        if (existingEvent != null) {
            eventName = existingEvent.eventName
            eventDescription = existingEvent.eventDescription
            location = existingEvent.location
            totalSlots = existingEvent.totalSlots.toString()
            formLink = existingEvent.formLink
        }
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text(if (isEditMode) "Edit Event" else "Add New Event") },
                navigationIcon = {
                    IconButton(onClick = { navController.popBackStack() }) {
                        Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Back")
                    }
                }
            )
        }
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .padding(16.dp)
                .verticalScroll(rememberScrollState()),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            OutlinedTextField(
                value = eventName,
                onValueChange = { eventName = it },
                label = { Text("Event Name") },
                modifier = Modifier.fillMaxWidth()
            )

            OutlinedTextField(
                value = eventDescription,
                onValueChange = { eventDescription = it },
                label = { Text("Event Description") },
                modifier = Modifier.fillMaxWidth(),
                minLines = 3
            )

            OutlinedTextField(
                value = location,
                onValueChange = { location = it },
                label = { Text("Location / Venue") },
                modifier = Modifier.fillMaxWidth()
            )

            OutlinedTextField(
                value = totalSlots,
                onValueChange = { totalSlots = it },
                label = { Text("Total Available Slots") },
                modifier = Modifier.fillMaxWidth(),
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number)
            )

            OutlinedTextField(
                value = formLink,
                onValueChange = { formLink = it },
                label = { Text("Google Form Registration Link") },
                modifier = Modifier.fillMaxWidth(),
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Uri)
            )

            Spacer(modifier = Modifier.weight(1f))

            Button(
                onClick = {
                    val slots = totalSlots.toLongOrNull()

                    if (
                        eventName.isNotBlank() &&
                        eventDescription.isNotBlank() &&
                        location.isNotBlank() &&
                        slots != null &&
                        formLink.isNotBlank()
                    ) {
                        if (isEditMode && existingEvent != null) {
                            // ✅ UPDATE (keep eventDate untouched!)
                            val updated = existingEvent.copy(
                                eventName = eventName,
                                eventDescription = eventDescription,
                                location = location,
                                totalSlots = slots,
                                formLink = formLink
                                // eventDate remains same inside existingEvent
                            )

                            eventViewModel.updateEvent(updated)
                            Toast.makeText(context, "Event updated successfully!", Toast.LENGTH_SHORT).show()
                            navController.popBackStack()
                        } else {
                            // ✅ CREATE NEW
                            val newEvent = Event(
                                eventName = eventName,
                                eventDescription = eventDescription,
                                location = location,
                                totalSlots = slots,
                                formLink = formLink
                                // eventDate is server timestamp (auto)
                            )

                            eventViewModel.addEvent(newEvent)
                            Toast.makeText(context, "Event created successfully!", Toast.LENGTH_SHORT).show()
                            navController.popBackStack()
                        }
                    } else {
                        Toast.makeText(context, "Please fill all fields correctly.", Toast.LENGTH_SHORT).show()
                    }
                },
                modifier = Modifier.fillMaxWidth()
            ) {
                Text(if (isEditMode) "UPDATE EVENT" else "CREATE EVENT")
            }
        }
    }
}
