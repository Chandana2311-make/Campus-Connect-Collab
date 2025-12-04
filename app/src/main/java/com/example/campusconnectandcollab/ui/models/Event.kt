package com.example.campusconnectandcollab.ui.models

/**
 * Represents the data structure for an event in Firestore.
 * This is the single source of truth for what an Event is in the app.
 * Make sure all fields match the fields in Firestore.
 */
data class Event(
    // Default values are needed for Firestore to map data back to the object
    val id: String = "",
    val eventName: String = "",
    val eventDescription: String = "",
    val eventDate: String = "",
    val totalSlots: Long = 0L,       // Use Long for numbers from Firestore
    val registeredCount: Long = 0L   // Use Long for numbers from Firestore
)
