package com.example.campusconnectandcollab.ui.screens

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.unit.dp
import com.example.campusconnectandcollab.ui.models.Event

@Composable
fun AddEditEventDialog(
    event: Event?,                // null for Add, existing event for Edit
    onDismiss: () -> Unit,
    onSave: (Event) -> Unit
) {
    var title by remember { mutableStateOf(event?.title ?: "") }
    var description by remember { mutableStateOf(event?.description ?: "") }
    var date by remember { mutableStateOf(event?.date ?: "") }
    var venue by remember { mutableStateOf(event?.venue ?: "") }
    var imageUrl by remember { mutableStateOf(event?.imageUrl ?: "") }

    AlertDialog(
        onDismissRequest = onDismiss,
        title = { Text(if (event != null) "Edit Event" else "Add Event") },
        text = {
            Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                OutlinedTextField(
                    value = title,
                    onValueChange = { title = it },
                    label = { Text("Title") }
                )
                OutlinedTextField(
                    value = description,
                    onValueChange = { description = it },
                    label = { Text("Description") }
                )
                OutlinedTextField(
                    value = date,
                    onValueChange = { date = it },
                    label = { Text("Date") }
                )
                OutlinedTextField(
                    value = venue,
                    onValueChange = { venue = it },
                    label = { Text("Venue") }
                )
                OutlinedTextField(
                    value = imageUrl,
                    onValueChange = { imageUrl = it },
                    label = { Text("Image URL") }
                )
            }
        },
        confirmButton = {
            Button(onClick = {
                val eventId = event?.id ?: System.currentTimeMillis().toString()
                onSave(
                    Event(
                        id = eventId,
                        name = title, // <-- Pass title as name to fix the error
                        title = title,
                        description = description,
                        date = date,
                        venue = venue,
                        imageUrl = imageUrl
                    )
                )
            }) {
                Text("Save")
            }
        },
        dismissButton = {
            Button(onClick = onDismiss) {
                Text("Cancel")
            }
        }
    )
}
