package com.example.campusconnectandcollab.ui.models

import com.google.firebase.Timestamp

data class Registration(
    val email: String = "",
    val registeredAt: Timestamp? = null
)
