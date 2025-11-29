package com.example.campusconnectandcollab.ui.screens

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.example.campusconnectandcollab.ui.models.Event

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AddEditEventDialog(
    event: Event?,
    onDismiss: () -> Unit,
    onSave: (Event) -> Unit
) {
    var title by remember { mutableStateOf(event?.title ?: "") }
    var description by remember { mutableStateOf(event?.description ?: "") }
    var date by remember { mutableStateOf(event?.date ?: "") }
    var venue by remember { mutableStateOf(event?.venue ?: "") }
    var maxParticipants by remember { mutableStateOf((event?.maxParticipants ?: 0).toString()) }

    AlertDialog(
        onDismissRequest = onDismiss,
        title = { Text(if (event == null) "Add event" else "Edit event") },
        text = {
            Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                OutlinedTextField(value = title, onValueChange = { title = it }, label = { Text("Title") })
                OutlinedTextField(value = description, onValueChange = { description = it }, label = { Text("Description") })
                OutlinedTextField(value = date, onValueChange = { date = it }, label = { Text("Date") })
                OutlinedTextField(value = venue, onValueChange = { venue = it }, label = { Text("Venue") })
                OutlinedTextField(value = maxParticipants, onValueChange = { maxParticipants = it }, label = { Text("Max participants") })
            }
        },
        confirmButton = {
            TextButton(onClick = {
                val mp = maxParticipants.toIntOrNull() ?: 0
                val newEvent = Event(
                    id = event?.id ?: "",
                    title = title,
                    description = description,
                    date = date,
                    venue = venue,
                    imageUrl = event?.imageUrl ?: "",
                    maxParticipants = mp,
                    name = title
                )
                onSave(newEvent)
            }) {
                Text("Save")
            }
        },
        dismissButton = {
            TextButton(onClick = onDismiss) { Text("Cancel") }
        }
    )
}
