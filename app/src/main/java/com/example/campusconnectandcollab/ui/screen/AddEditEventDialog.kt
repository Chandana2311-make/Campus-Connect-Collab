package com.example.campusconnectandcollab.ui.screen

// FIX: Separated the package and import statements onto their own lines.
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import com.example.campusconnectandcollab.ui.models.Event

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AddEditEventDialog(
    event: Event?, // The existing event to edit, or null to add a new one
    onDismiss: () -> Unit,
    onSave: (Event) -> Unit
) {
    var eventName by remember { mutableStateOf(event?.eventName ?: "") }
    var eventDescription by remember { mutableStateOf(event?.eventDescription ?: "") }
    var eventDate by remember { mutableStateOf(event?.eventDate ?: "") }
    var totalSlots by remember { mutableStateOf((event?.totalSlots ?: 0).toString()) }

    AlertDialog(
        onDismissRequest = onDismiss,
        title = { Text(if (event == null) "Add Event" else "Edit Event") },
        text = {
            Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                OutlinedTextField(
                    value = eventName,
                    onValueChange = { eventName = it },
                    label = { Text("Event Name") }
                )
                OutlinedTextField(
                    value = eventDescription,
                    onValueChange = { eventDescription = it },
                    label = { Text("Description") }
                )
                OutlinedTextField(
                    value = eventDate,
                    onValueChange = { eventDate = it },
                    label = { Text("Date (e.g., YYYY-MM-DD)") }
                )
                OutlinedTextField(
                    value = totalSlots,
                    onValueChange = { totalSlots = it },
                    label = { Text("Total Slots") },
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number)
                )
            }
        },
        confirmButton = {
            TextButton(onClick = {
                val newEvent = Event(
                    id = event?.id ?: "", // Keep the old ID if editing
                    eventName = eventName,
                    eventDescription = eventDescription,
                    eventDate = eventDate,
                    totalSlots = totalSlots.toLongOrNull() ?: 0L, // Convert to Long for Firestore
                    registeredCount = event?.registeredCount ?: 0L // Keep old count if editing
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
