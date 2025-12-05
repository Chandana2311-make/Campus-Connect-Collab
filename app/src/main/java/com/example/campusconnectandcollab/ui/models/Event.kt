package com.example.campusconnectandcollab.ui.models

import com.google.firebase.firestore.DocumentId

/**
 * Represents the data structure for an event in Firestore.
 * This is the single source of truth for what an Event is in the app.
 * Make sure all fields match the fields in Firestore.
 */
data class Event(
    // FIX: Changed all properties from 'val' to 'var' to allow Firestore to populate them.
    @DocumentId var id: String = "",

    var eventName: String = "",
    var eventDescription: String = "",
    var eventDate: String = "",
    var totalSlots: Long = 0L,
    var registeredCount: Long = 0L,
    var formLink: String = ""
)

