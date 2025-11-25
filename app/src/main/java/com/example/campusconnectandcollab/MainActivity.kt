package com.example.campusconnectandcollab

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.ui.Modifier
import androidx.navigation.compose.rememberNavController
import com.example.campusconnectandcollab.ui.theme.CampusConnectAndCollabTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            CampusConnectAndCollabTheme {

                val navController = rememberNavController()

                Scaffold { paddingValues ->
                    Surface(
                        modifier = Modifier
                            .fillMaxSize()
                            .padding(paddingValues),
                        color = MaterialTheme.colorScheme.background
                    ) {
                        // ✅ Use fully-qualified name to avoid ambiguity
                        com.example.campusconnectandcollab.ui.navigation.AppNavGraph(
                            navController = navController
                        )
                    }
                }
            }
        }
    }
}