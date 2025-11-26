package com.example.campusconnectandcollab.ui.navigation

// Define all routes in a SINGLE sealed class
sealed class AppRoute(val route: String) {

    // LOGIN
    object Login : AppRoute("login")

    // DASHBOARDS
    object StudentDashboard : AppRoute("student_dashboard")
    object AdminDashboard : AppRoute("admin_dashboard")

    // EVENT LIST SCREENS
    object StudentEventList : AppRoute("student_event_list")   // For students
    object AdminEventList : AppRoute("admin_event_list")       // For admin

    // EVENT DETAILS (for both)
    object EventDetail : AppRoute("event_detail/{eventId}") {
        fun createRoute(eventId: String) = "event_detail/$eventId"
    }

    // ADMIN – Add Event
    object AddEvent : AppRoute("add_event")

    // ADMIN – Edit Event
    object EditEvent : AppRoute("edit_event/{eventId}") {
        fun createRoute(eventId: String) = "edit_event/$eventId"
    }
}
