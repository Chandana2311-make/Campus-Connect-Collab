package com.example.campusconnectandcollab.ui.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.example.campusconnectandcollab.LoginScreen
import com.example.campusconnectandcollab.ui.screens.AdminDashboardScreen
import com.example.campusconnectandcollab.ui.screens.AdminEventsScreen
import com.example.campusconnectandcollab.ui.screens.StudentDashboardScreen
import com.example.campusconnectandcollab.ui.screens.StudentEventsScreen

@Composable
fun AppNavGraph(navController: NavHostController) {

    NavHost(
        navController = navController,
        startDestination = AppRoute.Login.route
    ) {

        // LOGIN SCREEN
        composable(AppRoute.Login.route) {
            LoginScreen { email, password, isAdmin ->
                if (isAdmin) {
                    navController.navigate(AppRoute.AdminDashboard.route) {
                        popUpTo(AppRoute.Login.route) { inclusive = true }
                    }
                } else {
                    navController.navigate(AppRoute.StudentDashboard.route) {
                        popUpTo(AppRoute.Login.route) { inclusive = true }
                    }
                }
            }
        }

        // STUDENT DASHBOARD
        composable(AppRoute.StudentDashboard.route) {
            StudentDashboardScreen(
                onEventsClick = {
                    navController.navigate(AppRoute.StudentEventList.route)
                }
            )
        }

        // STUDENT EVENT LIST (READ-ONLY)
        composable(AppRoute.StudentEventList.route) {
            StudentEventsScreen(navController)
        }

        // ADMIN DASHBOARD
        composable(AppRoute.AdminDashboard.route) {
            AdminDashboardScreen(navController)
        }

        // ADMIN EVENT LIST (WITH ADD/EDIT/DELETE)
        composable(AppRoute.AdminEventList.route) {
            AdminEventsScreen(navController)
        }

        // EVENT DETAILS
        composable(AppRoute.EventDetail.route) { backStackEntry ->
            val eventId = backStackEntry.arguments?.getString("eventId")
            // TODO: Add your EventDetailScreen(eventId)
        }

        // ADD EVENT (ADMIN ONLY)
        composable(AppRoute.AddEvent.route) {
            // TODO: Add AddEventScreen(navController)
        }

        // EDIT EVENT (ADMIN ONLY)
        composable(AppRoute.EditEvent.route) { backStackEntry ->
            val eventId = backStackEntry.arguments?.getString("eventId")
            // TODO: Add EditEventScreen(navController, eventId)
        }
    }
}
