package com.example.campusconnectandcollab.ui.navigation

import androidx.compose.runtime.Composable
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.example.campusconnectandcollab.LoginScreen
import com.example.campusconnectandcollab.ui.screens.AdminDashboardScreen
import com.example.campusconnectandcollab.ui.screens.AdminEventsScreen
import com.example.campusconnectandcollab.ui.screens.StudentDashboardScreen
import com.example.campusconnectandcollab.ui.screens.StudentEventsScreen
import com.example.campusconnectandcollab.ui.viewmodels.EventViewModel

@Composable
fun AppNavGraph(navController: NavHostController) {
    val eventViewModel: EventViewModel = viewModel()

    NavHost(navController = navController, startDestination = "login") {

        // ---------- LOGIN ----------
        composable("login") {
            LoginScreen { email, password, isAdmin ->
                if (isAdmin) {
                    navController.navigate("admin_dashboard") {
                        popUpTo("login") { inclusive = true }
                    }
                } else {
                    navController.navigate("student_dashboard") {
                        popUpTo("login") { inclusive = true }
                    }
                }
            }
        }

        // ---------- STUDENT ----------
        composable("student_dashboard") {
            StudentDashboardScreen(navController, eventViewModel)
        }

        composable("student_event_list") {
            StudentEventsScreen(navController, eventViewModel)
        }

        // ---------- ADMIN ----------
        composable("admin_dashboard") {
            AdminDashboardScreen(navController, eventViewModel)
        }

        composable("admin_event_list") {
            AdminEventsScreen(navController, eventViewModel)
        }

        // ---------- EVENT DETAIL ----------
        composable("event_detail/{eventId}") { backStackEntry ->
            val eventId = backStackEntry.arguments?.getString("eventId")
            // TODO: EventDetailScreen(eventId, eventViewModel)
        }
    }
}
