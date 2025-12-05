package com.example.campusconnectandcollab.ui.navigation

import androidx.compose.runtime.Composable
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.example.campusconnectandcollab.LoginScreen
import com.example.campusconnectandcollab.ui.screen.AddEventScreen
import com.example.campusconnectandcollab.ui.screen.AdminDashboardScreen
import com.example.campusconnectandcollab.ui.screen.StudentEventsScreen
import com.example.campusconnectandcollab.ui.screens.LostFoundScreen
import com.example.campusconnectandcollab.ui.viewmodels.EventViewModel

@Composable
fun AppNavGraph(navController: NavHostController) {

    // --- Create the ViewModel here, at the highest level. This is the correct pattern. ---
    val eventViewModel: EventViewModel = viewModel()

    // The start destination should always be the login screen.
    NavHost(navController = navController, startDestination = "login") {

        composable("login") {
            LoginScreen { _, _, selectedSystem, isAdmin ->
                when (selectedSystem) {
                    "events" -> {
                        val route = if (isAdmin) "admin_dashboard" else "student_events"
                        navController.navigate(route) {
                            // Clear the back stack so the user can't go back to login
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

        // --- Define all your other screens at the top level ---

        composable("admin_dashboard") {
            AdminDashboardScreen(
                navController = navController,
                eventViewModel = eventViewModel // Pass the single ViewModel instance
            )
        }

        composable("student_events") {
            StudentEventsScreen(
                navController = navController,
                eventViewModel = eventViewModel // Pass the single ViewModel instance
            )
        }

        composable("add_event") {
            AddEventScreen(
                navController = navController,
                eventViewModel = eventViewModel // Pass the single ViewModel instance
            )
        }

        composable("lost_found") {
            LostFoundScreen(navController = navController)
        }
    }
}
