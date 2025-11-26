package com.example.campusconnectandcollab.ui.models

data class Event(
    val id: String = "",
    val name: String = "",
    val title: String = "",
    val description: String = "",
    val date: String = "",
    val venue: String = "",
    val imageUrl: String = "",
    val maxParticipants: Int = 0
)
