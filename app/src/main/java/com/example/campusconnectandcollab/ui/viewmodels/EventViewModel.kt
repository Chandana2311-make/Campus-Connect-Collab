package com.example.campusconnectandcollab.ui.viewmodels

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.campusconnectandcollab.ui.models.Event
import com.google.firebase.firestore.FieldValue
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.Query
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.tasks.await

class EventViewModel : ViewModel() {

    private val firestore: FirebaseFirestore = Firebase.firestore

    private val _events = MutableStateFlow<List<Event>>(emptyList())
    val events: StateFlow<List<Event>> = _events

    private val _isLoading = MutableStateFlow(false)
    val isLoading: StateFlow<Boolean> = _isLoading

    init {
        fetchEvents()
    }

    fun fetchEvents() {
        _isLoading.value = true
        // --- RE-ENABLING SORTING ---
        // This will now work because your index, rules, and billing are correct.
        firestore.collection("events")
            .orderBy("eventDate", Query.Direction.DESCENDING)
            .addSnapshotListener { snapshot, error ->
                if (error != null) {
                    Log.e("FirestoreError", "Listen failed. Check Firestore rules and indexes.", error)
                    _isLoading.value = false
                    return@addSnapshotListener
                }

                if (snapshot == null) {
                    _events.value = emptyList()
                    _isLoading.value = false
                    return@addSnapshotListener
                }

                val eventList = mutableListOf<Event>()
                for (document in snapshot.documents) {
                    try {
                        val event = document.toObject(Event::class.java)
                        if (event != null) {
                            // This correctly assigns the unique ID to each event object
                            eventList.add(event.copy(id = document.id))
                        } else {
                            Log.w("FirestoreData", "Failed to convert document: ${document.id}")
                        }
                    } catch (e: Exception) {
                        Log.e("FirestoreData", "Error converting document ${document.id}. Check data types.", e)
                    }
                }

                _events.value = eventList
                _isLoading.value = false
            }
    }

    fun addEvent(event: Event) {
        viewModelScope.launch {
            try {
                firestore.collection("events").add(event).await()
                Log.d("FirestoreAction", "Successfully added a new event.")
            } catch (e: Exception) {
                Log.e("FirestoreAction", "Failed to add event.", e)
            }
        }
    }

    fun updateEvent(event: Event) {
        if (event.id.isBlank()) {
            Log.w("FirestoreAction", "Update failed: event ID is blank.")
            return
        }

        viewModelScope.launch {
            try {
                firestore.collection("events").document(event.id).set(event).await()
                Log.d("FirestoreAction", "Successfully updated event: ${event.id}")
            } catch (e: Exception) {
                Log.e("FirestoreAction", "Failed to update event: ${event.id}", e)
            }
        }
    }

    fun deleteEvent(eventId: String) {
        if (eventId.isBlank()) return
        viewModelScope.launch {
            try {
                firestore.collection("events").document(eventId).delete().await()
                Log.d("FirestoreAction", "Successfully deleted event: $eventId")
            } catch (e: Exception) {
                Log.e("FirestoreAction", "Failed to delete event: $eventId", e)
            }
        }
    }

    fun registerForEvent(eventId: String) {
        if (eventId.isBlank()) return

        viewModelScope.launch {
            try {
                val eventRef = firestore.collection("events").document(eventId)
                eventRef.update("registeredCount", FieldValue.increment(1)).await()
                Log.d("FirestoreAction", "Successfully incremented registration for event: $eventId")
            } catch (e: Exception) {
                Log.e("FirestoreAction", "Failed to register for event: $eventId", e)
            }
        }
    }
}
