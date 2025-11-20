package com.example.campusconnectandcollab.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.foundation.clickable
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.List
import androidx.compose.material.icons.filled.Person
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

@Composable
fun StudentDashboardScreen(
    onEventsClick: () -> Unit
) {

    var selectedTab by remember { mutableStateOf(0) }

    val tabs = listOf("Home", "My Events", "Profile")
    val tabIcons = listOf(Icons.Default.Home, Icons.Default.List, Icons.Default.Person)

    Scaffold(
        bottomBar = {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(Color(0xFF1565C0)) // Deep vibrant blue
                    .padding(vertical = 12.dp)
            ) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceEvenly,
                    verticalAlignment = Alignment.CenterVertically
                ) {

                    tabs.forEachIndexed { index, label ->
                        NavigationPillButton(
                            label = label,
                            icon = tabIcons[index],
                            selected = selectedTab == index,
                            onClick = { selectedTab = index }
                        )
                    }
                }
            }
        }
    ) { paddingValues ->

        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
        ) {
            when (selectedTab) {
                0 -> StudentHomeScreen()
                1 -> StudentMyEventsScreen(onEventsClick)
                2 -> StudentProfileScreen()
            }
        }
    }
}

@Composable
fun NavigationPillButton(
    label: String,
    icon: androidx.compose.ui.graphics.vector.ImageVector,
    selected: Boolean,
    onClick: () -> Unit
) {
    val backgroundColor = if (selected) Color.White else Color.Transparent
    val contentColor = if (selected) Color(0xFF1565C0) else Color.White

    Row(
        modifier = Modifier
            .background(
                color = backgroundColor,
                shape = RoundedCornerShape(20.dp)
            )
            .padding(horizontal = 18.dp, vertical = 8.dp)
            .clickable { onClick() },
        verticalAlignment = Alignment.CenterVertically
    ) {
        Icon(imageVector = icon, contentDescription = null, tint = contentColor)
        Spacer(modifier = Modifier.width(6.dp))
        Text(text = label, color = contentColor, fontSize = 14.sp)
    }
}

@Composable
fun StudentHomeScreen() {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(
                Brush.verticalGradient(
                    listOf(Color(0xFF42A5F5), Color(0xFF1E88E5))
                )
            )
            .padding(20.dp)
    ) {
        Text(
            "Upcoming Fests",
            color = Color.White,
            fontSize = 26.sp
        )
    }
}

@Composable
fun StudentMyEventsScreen(onEventsClick: () -> Unit) {
    Column(
        modifier = Modifier.fillMaxSize(),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Spacer(modifier = Modifier.height(30.dp))
        Text(
            text = "My Registered Events",
            fontSize = 22.sp,
            modifier = Modifier.clickable { onEventsClick() } // Navigate to event list
        )
    }
}

@Composable
fun StudentProfileScreen() {
    Column(
        modifier = Modifier.fillMaxSize(),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Spacer(modifier = Modifier.height(30.dp))
        Text(text = "Profile", fontSize = 22.sp)
    }
}
