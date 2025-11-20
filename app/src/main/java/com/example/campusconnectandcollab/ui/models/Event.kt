package com.example.campusconnectandcollab.ui.models

data class Event(
    val id: String,
    val name: String,        // Added name
    val title: String,
    val description: String,
    val date: String,
    val venue: String,
    val imageUrl: String
)

// Sample event list
val eventList = listOf(
    Event(
        id = "1",
        name = "Hackathon 2025",
        title = "Hackathon 2025",
        description = "A coding competition for students across the campus.",
        date = "Dec 5 · 10:00 AM",
        venue = "Auditorium A",
        imageUrl = "https://yourimageurl.com/hackathon.jpg"
    ),
    Event(
        id = "2",
        name = "RoboRace Championship",
        title = "RoboRace Championship",
        description = "Robot racing competition for all engineering students.",
        date = "Dec 12 · 11:00 AM",
        venue = "Mechanical Block",
        imageUrl = "https://yourimageurl.com/roborace.jpg"
    ),
    Event(
        id = "3",
        name = "Coding Contest",
        title = "Coding Contest",
        description = "Competitive programming contest for CSE students.",
        date = "Jan 3 · 9:00 AM",
        venue = "CSE Lab",
        imageUrl = "https://yourimageurl.com/codingcontest.jpg"
    )
)
