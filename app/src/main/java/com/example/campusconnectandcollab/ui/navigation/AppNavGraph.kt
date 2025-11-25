package com.example.campusconnectandcollab.ui.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.example.campusconnectandcollab.LoginScreen
import com.example.campusconnectandcollab.ui.screens.AdminDashboardScreen
import com.example.campusconnectandcollab.ui.screens.AdminEventsScreen
import com.example.campusconnectandcollab.ui.screens.StudentDashboardScreen   // ✅ FIXED IMPORT

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
                    navController.navigate(AppRoute.EventList.route)
                }
            )
        }

        // ADMIN DASHBOARD
        composable(AppRoute.AdminDashboard.route) {
            AdminDashboardScreen(navController)
        }

        // ADMIN EVENT LIST
        composable(AppRoute.EventList.route) {
            AdminEventsScreen(navController)
        }
    }
}
