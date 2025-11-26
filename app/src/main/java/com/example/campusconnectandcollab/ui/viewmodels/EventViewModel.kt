package com.example.campusconnectandcollab.ui.viewmodels

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import com.example.campusconnectandcollab.ui.models.Event
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import java.util.UUID

class EventViewModel(
    private val savedStateHandle: SavedStateHandle
) : ViewModel() {

    private val _events = MutableStateFlow<List<Event>>(emptyList())
    val events: StateFlow<List<Event>> = _events.asStateFlow()

    init {
        _events.value = sampleEvents()
    }

    private fun sampleEvents(): List<Event> = listOf(
        Event(
            id = "1",
            name = "Hackathon 2025",
            title = "Hackathon 2025",
            description = "A coding competition across the campus.",
            date = "Dec 5 · 10:00 AM",
            venue = "Auditorium A",
            imageUrl = "",
            maxParticipants = 100
        ),
        Event(
            id = "2",
            name = "RoboRace Championship",
            title = "RoboRace Championship",
            description = "Robot racing competition for engineering students.",
            date = "Dec 12 · 11:00 AM",
            venue = "Mechanical Block",
            imageUrl = "",
            maxParticipants = 50
        ),
        Event(
            id = "3",
            name = "Coding Contest",
            title = "Coding Contest",
            description = "Competitive programming contest.",
            date = "Jan 3 · 9:00 AM",
            venue = "CSE Lab",
            imageUrl = "",
            maxParticipants = 120
        )
    )

    fun addEvent(event: Event) {
        _events.value = _events.value + event
    }

    fun updateEvent(updated: Event) {
        _events.value = _events.value.map { if (it.id == updated.id) updated else it }
    }

    fun deleteEvent(event: Event) {
        _events.value = _events.value.filter { it.id != event.id }
    }

    fun nextId(): String = UUID.randomUUID().toString()
}
