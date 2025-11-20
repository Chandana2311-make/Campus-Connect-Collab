package com.example.campusconnectandcollab

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.ui.Modifier
import androidx.navigation.compose.rememberNavController
import com.example.campusconnectandcollab.ui.navigation.AppNavGraph
import com.example.campusconnectandcollab.ui.theme.CampusConnectAndCollabTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            CampusConnectAndCollabTheme {
                val navController = rememberNavController()

                // Use Scaffold if you want a top bar, bottom bar, etc.
                androidx.compose.material3.Scaffold { paddingValues ->
                    // Apply paddingValues to root container to fix the warning
                    Surface(
                        modifier = Modifier
                            .fillMaxSize()
                            .padding(paddingValues),
                        color = MaterialTheme.colorScheme.background
                    ) {
                        AppNavGraph(navController = navController)
                    }
                }
            }
        }
    }
}
