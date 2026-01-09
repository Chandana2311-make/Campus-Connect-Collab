package com.example.campusconnectandcollab.ui.viewmodels

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.campusconnectandcollab.ui.models.Event
import com.google.firebase.firestore.FieldValue
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.Query
import com.google.firebase.Firebase
import com.google.firebase.firestore.firestore
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
    private val _currentUserEmail = MutableStateFlow("")
    val currentUserEmail: StateFlow<String> = _currentUserEmail

    fun setCurrentUserEmail(email: String) {
        _currentUserEmail.value = email.trim()
    }

    fun fetchEvents() {
        _isLoading.value = true

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

    /**
     * ✅ Correct Firestore-safe register:
     * Uses atomic increment to avoid race conditions.
     */
    fun registerForEvent(eventId: String) {
        if (eventId.isBlank()) return

        val email = _currentUserEmail.value
        if (email.isBlank()) {
            Log.e("FirestoreAction", "No user email found. Cannot register.")
            return
        }

        // Use a safe Firestore document id (no weird chars)
        val userKey = email
            .lowercase()
            .replace(".", "_")
            .replace("@", "_at_")

        viewModelScope.launch {
            try {
                val eventRef = firestore.collection("events").document(eventId)
                val regRef = eventRef.collection("registrations").document(userKey)

                firestore.runTransaction { tx ->
                    val eventSnap = tx.get(eventRef)
                    val regSnap = tx.get(regRef)

                    val totalSlots = eventSnap.getLong("totalSlots") ?: 0L
                    val registeredCount = eventSnap.getLong("registeredCount") ?: 0L

                    // ✅ prevent double registration
                    if (regSnap.exists()) {
                        throw IllegalStateException("Already registered")
                    }

                    // ✅ prevent overflow
                    if (registeredCount >= totalSlots) {
                        throw IllegalStateException("Event full")
                    }

                    // ✅ create registration doc
                    tx.set(regRef, mapOf(
                        "email" to email,
                        "registeredAt" to FieldValue.serverTimestamp()
                    ))

                    // ✅ increment count atomically
                    tx.update(eventRef, "registeredCount", FieldValue.increment(1))

                    null
                }.await()

                Log.d("FirestoreAction", "Registered successfully for $eventId")
            } catch (e: Exception) {
                Log.e("FirestoreAction", "Register failed: ${e.message}", e)
            }
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
}
