package com.example.campusconnectandcollab.ui.navigation

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.example.campusconnectandcollab.LoginScreen
import com.example.campusconnectandcollab.ui.screen.AddEventScreen
import com.example.campusconnectandcollab.ui.screen.EventDetailScreen
import com.example.campusconnectandcollab.ui.screen.EventListScreen
import com.example.campusconnectandcollab.ui.screen.EventRegistrationsScreen
import com.example.campusconnectandcollab.ui.screen.StudentEventsScreen
import com.example.campusconnectandcollab.ui.screen.SplashScreen
import com.example.campusconnectandcollab.ui.screens.CreateAccountScreen
import com.example.campusconnectandcollab.ui.screens.LostFoundScreen
import com.example.campusconnectandcollab.ui.viewmodels.EventViewModel

@Composable
fun AppNavGraph(navController: NavHostController) {

    // ✅ FIX: removed the accidental "x"
    val eventViewModel: EventViewModel = viewModel()

    NavHost(navController = navController, startDestination = "splash") {

        // ✅ Splash → Home
        composable("splash") {
            SplashScreen(
                onDone = {
                    navController.navigate("home") {
                        popUpTo("splash") { inclusive = true }
                    }
                }
            )
        }

        // ✅ Home
        composable("home") {
            HomeScreen(
                onEventsClick = { navController.navigate("login") },
                onLostFoundClick = { navController.navigate("login") }
            )
        }

        // ✅ Signup
        composable("signup") {
            CreateAccountScreen(
                onBack = { navController.navigateUp() },
                onAccountCreated = {
                    navController.navigate("login") {
                        popUpTo("signup") { inclusive = true }
                    }
                }
            )
        }

        // ✅ Profile placeholder
        composable("profile") {
            ProfilePlaceholderScreen()
        }

        // ✅ Login
        composable("login") {
            LoginScreen(
                onCreateAccountClick = { navController.navigate("signup") }
            ) { email, _, selectedSystem, isAdmin ->

                // ✅ Store email (used for registration tracking)
                eventViewModel.setCurrentUserEmail(email)

                when (selectedSystem) {
                    "events" -> {
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

        // ✅ Admin dashboard
        composable("admin_dashboard") {
            EventListScreen(
                navController = navController,
                eventViewModel = eventViewModel
            )
        }

        // ✅ Student events list
        composable("student_events") {
            StudentEventsScreen(
                navController = navController,
                eventViewModel = eventViewModel
            )
        }

        // ✅ Add event
        composable("add_event") {
            AddEventScreen(
                navController = navController,
                eventViewModel = eventViewModel
            )
        }

        // ✅ Edit event
        composable("edit_event/{eventId}") { backStackEntry ->
            val eventId = backStackEntry.arguments?.getString("eventId") ?: return@composable
            AddEventScreen(
                navController = navController,
                eventViewModel = eventViewModel,
                eventId = eventId
            )
        }

        // ✅ Event detail
        composable("event_detail/{eventId}") { backStackEntry ->
            val eventId = backStackEntry.arguments?.getString("eventId") ?: return@composable
            EventDetailScreen(
                eventId = eventId,
                navController = navController,
                eventViewModel = eventViewModel
            )
        }

        // ✅ Registrations list (admin)
        composable("event_regs/{eventId}") { backStackEntry ->
            val eventId = backStackEntry.arguments?.getString("eventId") ?: return@composable
            EventRegistrationsScreen(
                eventId = eventId,
                navController = navController
            )
        }

        // ✅ Lost & Found
        composable("lost_found") {
            LostFoundScreen(navController = navController)
        }
    }
}

/* -------------------- HOME UI (same as your previous) -------------------- */

@Composable
private fun HomeScreen(
    onEventsClick: () -> Unit,
    onLostFoundClick: () -> Unit
) {
    val bg = Brush.verticalGradient(
        colors = listOf(
            Color(0xFFF7F1FF),
            Color(0xFFFFF1F7)
        )
    )

    val scrollState = rememberScrollState()

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(bg)
            .verticalScroll(scrollState)
            .padding(18.dp),
        verticalArrangement = Arrangement.spacedBy(14.dp)
    ) {
        Text(
            text = "Welcome",
            style = MaterialTheme.typography.headlineSmall.copy(fontWeight = FontWeight.Bold),
            color = MaterialTheme.colorScheme.onBackground
        )

        Text(
            text = "Explore campus events, join activities, and access lost & found in one place.",
            style = MaterialTheme.typography.bodyMedium,
            color = MaterialTheme.colorScheme.onBackground.copy(alpha = 0.75f)
        )

        Spacer(modifier = Modifier.height(6.dp))

        FeatureCard(
            title = "Event Updates",
            subtitle = "Discover events, join and participate (Playo-style).",
            cta = "Continue to Login",
            onClick = onEventsClick
        )

        FeatureCard(
            title = "Lost & Found",
            subtitle = "Post items, browse found items, and contact owners.",
            cta = "Continue to Login",
            onClick = onLostFoundClick
        )

        Text(
            text = "Quick Highlights",
            style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.SemiBold)
        )

        InfoStrip(text = "• New events updated daily")
        InfoStrip(text = "• Join sports and club activities")
        InfoStrip(text = "• Report lost items quickly")

        Spacer(modifier = Modifier.height(10.dp))

        Text(
            text = "Created by",
            style = MaterialTheme.typography.titleSmall.copy(fontWeight = FontWeight.SemiBold),
            color = MaterialTheme.colorScheme.onBackground.copy(alpha = 0.85f)
        )
        Text(
            text = "Adhesh, Chandana HM, Chandana P, Bhavana",
            style = MaterialTheme.typography.bodyMedium,
            color = MaterialTheme.colorScheme.onBackground.copy(alpha = 0.75f)
        )

        Spacer(modifier = Modifier.height(22.dp))
    }
}

@Composable
private fun FeatureCard(
    title: String,
    subtitle: String,
    cta: String,
    onClick: () -> Unit
) {
    Card(
        shape = RoundedCornerShape(18.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White.copy(alpha = 0.95f)),
        elevation = CardDefaults.cardElevation(defaultElevation = 3.dp),
        modifier = Modifier.fillMaxWidth()
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Text(
                text = title,
                style = MaterialTheme.typography.titleLarge.copy(fontWeight = FontWeight.Bold)
            )
            Spacer(modifier = Modifier.height(6.dp))
            Text(
                text = subtitle,
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.75f)
            )
            Spacer(modifier = Modifier.height(14.dp))
            Button(
                onClick = onClick,
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(14.dp)
            ) {
                Text(cta)
            }
        }
    }
}

@Composable
private fun InfoStrip(text: String) {
    Card(
        shape = RoundedCornerShape(14.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White.copy(alpha = 0.85f)),
        elevation = CardDefaults.cardElevation(defaultElevation = 1.dp),
        modifier = Modifier.fillMaxWidth()
    ) {
        Text(
            text = text,
            modifier = Modifier.padding(12.dp),
            style = MaterialTheme.typography.bodyMedium
        )
    }
}

@Composable
private fun ProfilePlaceholderScreen() {
    Box(
        modifier = Modifier
            .fillMaxSize()
            .padding(18.dp),
        contentAlignment = Alignment.Center
    ) {
        Text(
            text = "Profile (Coming Soon)",
            style = MaterialTheme.typography.titleLarge.copy(fontWeight = FontWeight.Bold)
        )
    }
}
