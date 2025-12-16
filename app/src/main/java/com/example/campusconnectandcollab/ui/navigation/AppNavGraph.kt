package com.example.campusconnectandcollab.ui.navigation

import androidx.compose.runtime.Composable
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.example.campusconnectandcollab.LoginScreen
import com.example.campusconnectandcollab.ui.screen.AddEventScreen
import com.example.campusconnectandcollab.ui.screen.EventListScreen
import com.example.campusconnectandcollab.ui.screen.StudentEventsScreen
import com.example.campusconnectandcollab.ui.screens.LostFoundScreen
import com.example.campusconnectandcollab.ui.viewmodels.EventViewModel

@Composable
fun AppNavGraph(navController: NavHostController) {

    val eventViewModel: EventViewModel = viewModel()

    NavHost(navController = navController, startDestination = "login") {

        composable("login") {
            LoginScreen { _, _, selectedSystem, isAdmin ->
                when (selectedSystem) {
                    "events" -> {
                        // --- THIS IS THE FIX ---
                        // We tell the ViewModel to start fetching events RIGHT BEFORE we navigate.
                        eventViewModel.fetchEvents()

                        val route = if (isAdmin) "admin_dashboard" else "student_events"
                        navController.navigate(route) {
                            popUpTo("login") { inclusive = true }
                        }
                    }
                    "lost_found" -> {
                        navController.navigate("lost_found") {
                            popUpTo("login") { inclusive = true }
                        }
                    }
                }
            }
        }

        composable("admin_dashboard") {
            EventListScreen(
                navController = navController,
                eventViewModel = eventViewModel
            )
        }

        composable("student_events") {
            StudentEventsScreen(
                navController = navController,
                eventViewModel = eventViewModel
            )
        }

        composable("add_event") {
            AddEventScreen(
                navController = navController,
                eventViewModel = eventViewModel
            )
        }

        composable("lost_found") {
            LostFoundScreen(navController = navController)
        }
    }
}
