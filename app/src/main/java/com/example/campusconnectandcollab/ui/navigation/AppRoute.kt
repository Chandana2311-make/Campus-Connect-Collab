package com.example.campusconnectandcollab.ui.navigation

// Define all app routes in one place
sealed class AppRoute(val route: String) {

    object Login : AppRoute("login")

    object StudentDashboard : AppRoute("student_dashboard")

    object AdminDashboard : AppRoute("admin_dashboard")

    object EventList : AppRoute("event_list")

    object EventDetail : AppRoute("event_detail/{eventId}") {
        fun createRoute(eventId: String) = "event_detail/$eventId"
    }

    // Add Event Screen
    object AddEvent : AppRoute("add_event")

    // Edit Event Screen (requires eventId)
    object EditEvent : AppRoute("edit_event/{eventId}") {
        fun createRoute(eventId: String) = "edit_event/$eventId"
    }
}
