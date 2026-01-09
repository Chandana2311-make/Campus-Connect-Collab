package com.example.campusconnectandcollab.data

import com.google.gson.Gson
import com.google.gson.reflect.TypeToken

object EventStore {
    private val gson = Gson()

    fun toJson(events: List<DemoEvent>): String = gson.toJson(events)

    fun fromJson(json: String): List<DemoEvent> {
        val type = object : TypeToken<List<DemoEvent>>() {}.type
        return gson.fromJson(json, type) ?: emptyList()
    }
}
