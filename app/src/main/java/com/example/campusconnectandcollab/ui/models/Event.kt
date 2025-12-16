package com.example.campusconnectandcollab.ui.models

// 1. IMPORT THESE TWO LIBRARIES
import com.google.firebase.Timestamp
import com.google.firebase.firestore.ServerTimestamp

data class Event(
    var id: String = "",
    val eventName: String = "",
    val eventDescription: String = "",

    // 2. THIS IS THE ANSWER. THIS IS HOW AND WHERE YOU ADD THE TIMESTAMP.
    @ServerTimestamp
    val eventDate: Timestamp? = null, // Change the data type and add the annotation

    // Make sure all other fields your app uses are listed here
    val location: String = "",
    val totalSlots: Long = 0,
    val formLink: String = "",
    val organizer: String = "Admin", // Default value
    val registeredCount: Long = 0,
    val eventImageUrl: String = "",
    val isCompleted: Boolean = false
)
