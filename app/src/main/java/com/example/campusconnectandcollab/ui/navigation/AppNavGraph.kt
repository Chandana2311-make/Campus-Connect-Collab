package com.example.campusconnectandcollab.ui.navigation

import androidx.compose.runtime.Composable
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.example.campusconnectandcollab.LoginScreen
// FIX: Using the correct, specific package for each screen file.
import com.example.campusconnectandcollab.ui.screen.AddEventScreen
import com.example.campusconnectandcollab.ui.screen.EventListScreen
import com.example.campusconnectandcollab.ui.screens.LostFoundScreen // This one is in 'screens'
import com.example.campusconnectandcollab.ui.viewmodels.EventViewModel

@Composable
fun AppNavGraph(navController: NavHostController) {

    val eventViewModel: EventViewModel = viewModel()

    NavHost(navController = navController, startDestination = "login") {

        composable("login") {
            LoginScreen { _, _, selectedSystem, isAdmin ->
                when (selectedSystem) {
                    "events" -> {
                        val route = if (isAdmin) "admin_event_list" else "student_event_list"
                        navController.navigate(route) {
                            popUpTo("login") { inclusive = true }
                        }
                    }
                    "lost_found" -> {
                        navController.navigate("lostfound_home") {
                            popUpTo("login") { inclusive = true }
                        }
                    }
                }
            }
        }

        composable("admin_event_list") {
            EventListScreen(
                navController = navController,
                eventViewModel = eventViewModel,
                isAdmin = true
            )
        }

        composable("student_event_list") {
            EventListScreen(
                navController = navController,
                eventViewModel = eventViewModel,
                isAdmin = false
            )
        }

        composable("add_event") {
            AddEventScreen(navController = navController)
        }

        composable("lostfound_home") {
            LostFoundScreen(navController = navController)
        }
    }
}
