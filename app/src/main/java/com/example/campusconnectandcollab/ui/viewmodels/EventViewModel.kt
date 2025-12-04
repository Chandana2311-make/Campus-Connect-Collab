package com.example.campusconnectandcollab.ui.viewmodels

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.campusconnectandcollab.ui.models.Event
import com.google.firebase.firestore.FieldValue
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.Query
import com.google.firebase.firestore.toObject
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.tasks.await

class EventViewModel : ViewModel() {

    private val firestore = FirebaseFirestore.getInstance()
    private val eventsCollection = firestore.collection("events")

    private val _events = MutableStateFlow<List<Event>>(emptyList())
    val events: StateFlow<List<Event>> = _events

    init {
        fetchEvents()
    }

    // FIX: Renamed from refreshEvents() to fetchEvents() and implemented properly
    fun fetchEvents() {
        viewModelScope.launch {
            eventsCollection
                .orderBy("timestamp", Query.Direction.DESCENDING)
                .addSnapshotListener { snapshot, error ->
                    if (error != null) {
                        Log.e("EventViewModel", "Listen failed.", error)
                        return@addSnapshotListener
                    }

                    if (snapshot != null) {
                        val eventList = snapshot.documents.mapNotNull { doc ->
                            doc.toObject<Event>()?.copy(id = doc.id)
                        }
                        _events.value = eventList
                        Log.d("EventViewModel", "Events updated: ${eventList.size} events found.")
                    }
                }
        }
    }

    // FIX: Added addEvent function
    fun addEvent(event: Event) {
        viewModelScope.launch {
            try {
                // Add a server timestamp for ordering
                val eventWithTimestamp = event.toHashMap()
                eventWithTimestamp["timestamp"] = FieldValue.serverTimestamp()
                eventsCollection.add(eventWithTimestamp).await()
            } catch (e: Exception) {
                Log.e("EventViewModel", "Error adding event", e)
            }
        }
    }

    // FIX: Added updateEvent function
    fun updateEvent(event: Event) {
        viewModelScope.launch {
            try {
                eventsCollection.document(event.id).set(event).await()
            } catch (e: Exception) {
                Log.e("EventViewModel", "Error updating event", e)
            }
        }
    }

    // FIX: Added deleteEvent function
    fun deleteEvent(eventId: String) {
        viewModelScope.launch {
            try {
                eventsCollection.document(eventId).delete().await()
            } catch (e: Exception) {
                Log.e("EventViewModel", "Error deleting event", e)
            }
        }
    }

    // Helper to convert Event data class to a HashMap for adding a timestamp
    private fun Event.toHashMap(): HashMap<String, Any> {
        return hashMapOf(
            "eventName" to this.eventName,
            "eventDescription" to this.eventDescription,
            "eventDate" to this.eventDate,
            "totalSlots" to this.totalSlots,
            "registeredCount" to this.registeredCount
        )
    }
}
